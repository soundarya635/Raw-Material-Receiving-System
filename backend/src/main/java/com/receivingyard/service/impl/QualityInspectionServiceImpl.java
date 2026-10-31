package com.receivingyard.service.impl;

import com.receivingyard.dto.DispositionRequestDTO;
import com.receivingyard.dto.QualityInspectionRequestDTO;
import com.receivingyard.dto.QualityInspectionResponseDTO;
import com.receivingyard.dto.TestParameterDTO;
import com.receivingyard.entity.InspectionResult;
import com.receivingyard.entity.InventoryItem;
import com.receivingyard.entity.Lot;
import com.receivingyard.entity.QualityInspection;
import com.receivingyard.exception.BusinessValidationException;
import com.receivingyard.exception.ResourceNotFoundException;
import com.receivingyard.repository.InventoryRepository;
import com.receivingyard.repository.LotRepository;
import com.receivingyard.repository.QualityInspectionRepository;
import com.receivingyard.service.QualityInspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QualityInspectionServiceImpl implements QualityInspectionService {

    private final QualityInspectionRepository qualityInspectionRepository;
    private final LotRepository lotRepository;
    private final InventoryRepository inventoryRepository;

    @Autowired
    public QualityInspectionServiceImpl(QualityInspectionRepository qualityInspectionRepository,
                                        LotRepository lotRepository,
                                        InventoryRepository inventoryRepository) {
        this.qualityInspectionRepository = qualityInspectionRepository;
        this.lotRepository = lotRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    @Transactional
    public QualityInspectionResponseDTO recordInspection(QualityInspectionRequestDTO request) {
        Lot lot = lotRepository.findByLotNumber(request.getLotNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Lot", "lotNumber", request.getLotNumber()));

        QualityInspection inspection = new QualityInspection();
        inspection.setLotNumber(lot.getLotNumber());
        inspection.setInspectorName(request.getInspectorName() != null ? request.getInspectorName() : "Quality Inspector");
        inspection.setInspectionDate(Instant.now());
        inspection.setNotes(request.getNotes());

        boolean allPass = true;
        for (TestParameterDTO testDTO : request.getTests()) {
            InspectionResult result = new InspectionResult();
            result.setTestParameter(testDTO.getTestParameter().trim());
            result.setMinValue(testDTO.getMinValue());
            result.setMaxValue(testDTO.getMaxValue());
            result.setActualValue(testDTO.getActualValue());
            result.setUnit(testDTO.getUnit() != null ? testDTO.getUnit().trim() : lot.getUnit());

            // Inclusive bounds: minValue <= actualValue <= maxValue
            boolean isPass = testDTO.getActualValue().compareTo(testDTO.getMinValue()) >= 0
                    && testDTO.getActualValue().compareTo(testDTO.getMaxValue()) <= 0;
            result.setPass(isPass);

            if (!isPass) {
                allPass = false;
            }
            inspection.addResult(result);
        }

        inspection.setOverallResult(allPass ? "PASS" : "FAIL");
        QualityInspection saved = qualityInspectionRepository.save(inspection);

        // Update lot status to IN_INSPECTION
        lot.setStatus("IN_INSPECTION");
        lot.setUpdatedAt(Instant.now());
        lotRepository.save(lot);

        return mapToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QualityInspectionResponseDTO> getAllInspections() {
        return qualityInspectionRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public QualityInspectionResponseDTO getInspectionByLotNumber(String lotNumber) {
        QualityInspection inspection = qualityInspectionRepository.findTopByLotNumberOrderByCreatedAtDesc(lotNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Quality Inspection", "lotNumber", lotNumber));
        return mapToDTO(inspection);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lot> getQuarantinedLots() {
        return lotRepository.findAllByOrderByCreatedAtDesc().stream()
                .filter(l -> "QUARANTINED".equalsIgnoreCase(l.getStatus()) || "IN_INSPECTION".equalsIgnoreCase(l.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Lot submitDisposition(DispositionRequestDTO request) {
        Lot lot = lotRepository.findByLotNumber(request.getLotNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Lot", "lotNumber", request.getLotNumber()));

        // Check if there is an inspection on record
        Optional<QualityInspection> latestInspection = qualityInspectionRepository.findTopByLotNumberOrderByCreatedAtDesc(lot.getLotNumber());
        if (latestInspection.isEmpty()) {
            throw new BusinessValidationException("Cannot authorize disposition: No quality inspection recorded for lot " + lot.getLotNumber());
        }

        // Rule 5: Failed mandatory tests prevent full approval
        if ("APPROVE_FULL".equalsIgnoreCase(request.getDecision()) && "FAIL".equalsIgnoreCase(latestInspection.get().getOverallResult())) {
            throw new BusinessValidationException("Approval blocked! Quality tests failed for lot " + lot.getLotNumber() + ". Full approval is not permitted.");
        }

        BigDecimal accepted = request.getAcceptedQuantity() != null ? request.getAcceptedQuantity() : BigDecimal.ZERO;
        BigDecimal rejected = request.getRejectedQuantity() != null ? request.getRejectedQuantity() : BigDecimal.ZERO;

        // Rule 7: Accepted + Rejected + Pending must equal Received
        BigDecimal totalAccounted = accepted.add(rejected);
        if (totalAccounted.compareTo(lot.getReceivedQuantity()) > 0) {
            throw new BusinessValidationException(String.format(
                    "Total of accepted (%s) and rejected (%s) exceeds received quantity (%s)!",
                    accepted, rejected, lot.getReceivedQuantity()));
        }

        BigDecimal pending = lot.getReceivedQuantity().subtract(totalAccounted);

        // Determine new lot status
        String newStatus;
        if (accepted.compareTo(lot.getReceivedQuantity()) == 0) {
            newStatus = "FULLY_APPROVED";
        } else if (rejected.compareTo(lot.getReceivedQuantity()) == 0) {
            newStatus = "REJECTED";
        } else if (accepted.compareTo(BigDecimal.ZERO) > 0 && rejected.compareTo(BigDecimal.ZERO) > 0) {
            newStatus = "PARTIALLY_APPROVED";
        } else if (pending.compareTo(BigDecimal.ZERO) > 0) {
            newStatus = "QUARANTINED";
        } else {
            newStatus = "DISPOSITIONED";
        }

        lot.setAcceptedQuantity(accepted);
        lot.setRejectedQuantity(rejected);
        lot.setQuarantinedQuantity(pending);
        lot.setStatus(newStatus);
        lot.setUpdatedAt(Instant.now());
        Lot updatedLot = lotRepository.save(lot);

        // Update inventory record
        InventoryItem inventory = inventoryRepository.findByLotNumber(lot.getLotNumber())
                .orElseGet(() -> {
                    InventoryItem item = new InventoryItem();
                    item.setLotNumber(lot.getLotNumber());
                    item.setMaterial(lot.getMaterial());
                    item.setReceivedQuantity(lot.getReceivedQuantity());
                    item.setUnit(lot.getUnit());
                    return item;
                });

        // Rule 8: Only approved quantities can be released into production-available stock
        // Rule 10: Quarantined and rejected quantities must NEVER enter available production stock
        inventory.setAcceptedQuantity(accepted);
        inventory.setRejectedQuantity(rejected);
        inventory.setPendingQuantity(pending);
        inventory.setProductionAvailableQuantity(accepted); // ONLY accepted is available to production!
        inventory.setStatus(newStatus);
        inventory.setUpdatedAt(Instant.now());
        inventoryRepository.save(inventory);

        return updatedLot;
    }

    private QualityInspectionResponseDTO mapToDTO(QualityInspection inspection) {
        QualityInspectionResponseDTO dto = new QualityInspectionResponseDTO();
        dto.setId(inspection.getId());
        dto.setLotNumber(inspection.getLotNumber());
        dto.setInspectorName(inspection.getInspectorName());
        dto.setInspectionDate(inspection.getInspectionDate());
        dto.setOverallResult(inspection.getOverallResult());
        dto.setNotes(inspection.getNotes());
        dto.setCreatedAt(inspection.getCreatedAt());

        if (inspection.getResults() != null) {
            List<TestParameterDTO> results = inspection.getResults().stream()
                    .map(r -> {
                        TestParameterDTO t = new TestParameterDTO();
                        t.setTestParameter(r.getTestParameter());
                        t.setMinValue(r.getMinValue());
                        t.setMaxValue(r.getMaxValue());
                        t.setActualValue(r.getActualValue());
                        t.setUnit(r.getUnit());
                        t.setPass(r.isPass());
                        return t;
                    })
                    .collect(Collectors.toList());
            dto.setResults(results);
        }
        return dto;
    }
}

