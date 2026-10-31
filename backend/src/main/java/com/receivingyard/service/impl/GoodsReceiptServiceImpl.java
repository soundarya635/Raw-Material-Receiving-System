package com.receivingyard.service.impl;

import com.receivingyard.dto.GoodsReceiptRequestDTO;
import com.receivingyard.dto.GoodsReceiptResponseDTO;
import com.receivingyard.entity.GoodsReceipt;
import com.receivingyard.entity.InventoryItem;
import com.receivingyard.entity.Lot;
import com.receivingyard.entity.PurchaseOrder;
import com.receivingyard.exception.BusinessValidationException;
import com.receivingyard.exception.ResourceNotFoundException;
import com.receivingyard.repository.GoodsReceiptRepository;
import com.receivingyard.repository.InventoryRepository;
import com.receivingyard.repository.LotRepository;
import com.receivingyard.repository.PurchaseOrderRepository;
import com.receivingyard.service.GoodsReceiptService;
import com.receivingyard.validation.ToleranceCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class GoodsReceiptServiceImpl implements GoodsReceiptService {

    private final GoodsReceiptRepository goodsReceiptRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final LotRepository lotRepository;
    private final InventoryRepository inventoryRepository;

    @Autowired
    public GoodsReceiptServiceImpl(GoodsReceiptRepository goodsReceiptRepository,
                                   PurchaseOrderRepository purchaseOrderRepository,
                                   LotRepository lotRepository,
                                   InventoryRepository inventoryRepository) {
        this.goodsReceiptRepository = goodsReceiptRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.lotRepository = lotRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    @Transactional
    public GoodsReceiptResponseDTO recordReceipt(GoodsReceiptRequestDTO request) {
        // 1. Check duplicate GRN
        if (goodsReceiptRepository.existsByGrnNumber(request.getGrnNumber())) {
            throw new BusinessValidationException("Goods Receipt already exists with GRN: " + request.getGrnNumber());
        }

        // 2. Match with Purchase Order
        PurchaseOrder po = purchaseOrderRepository.findByPoNumber(request.getPoNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", "poNumber", request.getPoNumber()));

        // 3. Validate received quantity against PO tolerance (inclusive boundaries)
        boolean withinTolerance = ToleranceCalculator.isWithinTolerance(
                po.getOrderedQuantity(),
                request.getReceivedQuantity(),
                po.getTolerancePercentage()
        );

        String discrepancyReason = null;
        if (!withinTolerance) {
            BigDecimal minAllowed = ToleranceCalculator.calculateMinAllowed(po.getOrderedQuantity(), po.getTolerancePercentage());
            BigDecimal maxAllowed = ToleranceCalculator.calculateMaxAllowed(po.getOrderedQuantity(), po.getTolerancePercentage());
            if (request.getReceivedQuantity().compareTo(minAllowed) < 0) {
                discrepancyReason = String.format("Short delivery! Received %s %s is below inclusive minimum allowed of %s %s (-%s%%)",
                        request.getReceivedQuantity(), po.getUnit(), minAllowed, po.getUnit(), po.getTolerancePercentage());
            } else {
                discrepancyReason = String.format("Over delivery! Received %s %s exceeds inclusive maximum allowed of %s %s (+%s%%)",
                        request.getReceivedQuantity(), po.getUnit(), maxAllowed, po.getUnit(), po.getTolerancePercentage());
            }
        }

        // 4. Generate unique lot number
        String lotNumber = generateUniqueLotNumber();

        // 5. Create Goods Receipt
        GoodsReceipt grn = new GoodsReceipt();
        grn.setGrnNumber(request.getGrnNumber().trim());
        grn.setPoNumber(po.getPoNumber());
        grn.setSupplier(po.getSupplierName());
        grn.setMaterial(po.getMaterialName());
        grn.setTruckNumber(request.getTruckNumber().trim());
        grn.setReceivedQuantity(request.getReceivedQuantity());
        grn.setUnit(request.getUnit() != null && !request.getUnit().isBlank() ? request.getUnit().trim() : po.getUnit());
        grn.setArrivalDate(request.getArrivalDate() != null ? request.getArrivalDate() : LocalDate.now());
        grn.setWithinTolerance(withinTolerance);
        grn.setDiscrepancyReason(discrepancyReason);
        grn.setLotNumber(lotNumber);

        GoodsReceipt savedGrn = goodsReceiptRepository.save(grn);

        // 6. Mandatory Rule: Every new lot starts in QUARANTINED status
        Lot lot = new Lot();
        lot.setLotNumber(lotNumber);
        lot.setGrnNumber(savedGrn.getGrnNumber());
        lot.setPoNumber(po.getPoNumber());
        lot.setMaterial(po.getMaterialName());
        lot.setReceivedQuantity(request.getReceivedQuantity());
        lot.setQuarantinedQuantity(request.getReceivedQuantity());
        lot.setAcceptedQuantity(BigDecimal.ZERO);
        lot.setRejectedQuantity(BigDecimal.ZERO);
        lot.setUnit(grn.getUnit());
        lot.setStatus("QUARANTINED");
        lotRepository.save(lot);

        // 7. Mandatory Rule: Quarantined stock must NOT be added to production-available inventory
        InventoryItem inventory = new InventoryItem();
        inventory.setLotNumber(lotNumber);
        inventory.setMaterial(po.getMaterialName());
        inventory.setReceivedQuantity(request.getReceivedQuantity());
        inventory.setPendingQuantity(request.getReceivedQuantity()); // In quarantine
        inventory.setAcceptedQuantity(BigDecimal.ZERO);
        inventory.setRejectedQuantity(BigDecimal.ZERO);
        inventory.setProductionAvailableQuantity(BigDecimal.ZERO); // LOCKED (0.000)
        inventory.setUnit(grn.getUnit());
        inventory.setStatus("QUARANTINED");
        inventoryRepository.save(inventory);

        return mapToDTO(savedGrn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoodsReceiptResponseDTO> getAllReceipts() {
        return goodsReceiptRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public GoodsReceiptResponseDTO getReceiptByGrnNumber(String grnNumber) {
        GoodsReceipt grn = goodsReceiptRepository.findByGrnNumber(grnNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Goods Receipt", "grnNumber", grnNumber));
        return mapToDTO(grn);
    }

    private String generateUniqueLotNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String candidate;
        do {
            int randomNum = ThreadLocalRandom.current().nextInt(1000, 9999);
            candidate = "LOT-" + datePart + "-" + randomNum;
        } while (lotRepository.existsByLotNumber(candidate));
        return candidate;
    }

    private GoodsReceiptResponseDTO mapToDTO(GoodsReceipt grn) {
        GoodsReceiptResponseDTO dto = new GoodsReceiptResponseDTO();
        dto.setId(grn.getId());
        dto.setGrnNumber(grn.getGrnNumber());
        dto.setPoNumber(grn.getPoNumber());
        dto.setSupplier(grn.getSupplier());
        dto.setMaterial(grn.getMaterial());
        dto.setTruckNumber(grn.getTruckNumber());
        dto.setReceivedQuantity(grn.getReceivedQuantity());
        dto.setUnit(grn.getUnit());
        dto.setArrivalDate(grn.getArrivalDate());
        dto.setWithinTolerance(grn.isWithinTolerance());
        dto.setDiscrepancyReason(grn.getDiscrepancyReason());
        dto.setLotNumber(grn.getLotNumber());
        dto.setCreatedAt(grn.getCreatedAt());
        return dto;
    }
}

