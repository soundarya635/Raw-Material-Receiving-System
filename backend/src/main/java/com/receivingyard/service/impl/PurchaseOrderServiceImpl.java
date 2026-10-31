package com.receivingyard.service.impl;

import com.receivingyard.dto.PurchaseOrderRequestDTO;
import com.receivingyard.dto.PurchaseOrderResponseDTO;
import com.receivingyard.entity.PurchaseOrder;
import com.receivingyard.exception.BusinessValidationException;
import com.receivingyard.exception.ResourceNotFoundException;
import com.receivingyard.repository.PurchaseOrderRepository;
import com.receivingyard.service.PurchaseOrderService;
import com.receivingyard.validation.ToleranceCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    public PurchaseOrderServiceImpl(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @Override
    @Transactional
    public PurchaseOrderResponseDTO createPurchaseOrder(PurchaseOrderRequestDTO request) {
        if (purchaseOrderRepository.existsByPoNumber(request.getPoNumber())) {
            throw new BusinessValidationException("Purchase Order already exists with number: " + request.getPoNumber());
        }

        PurchaseOrder po = new PurchaseOrder();
        po.setPoNumber(request.getPoNumber().trim());
        po.setSupplierName(request.getSupplierName().trim());
        po.setMaterialName(request.getMaterialName().trim());
        po.setOrderedQuantity(request.getOrderedQuantity());
        po.setUnit(request.getUnit().trim());
        po.setTolerancePercentage(request.getTolerancePercentage() != null ? request.getTolerancePercentage() : BigDecimal.valueOf(5.00));
        po.setStatus("OPEN");

        PurchaseOrder saved = purchaseOrderRepository.save(po);
        return mapToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderResponseDTO> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderResponseDTO getPurchaseOrderByPoNumber(String poNumber) {
        PurchaseOrder po = purchaseOrderRepository.findByPoNumber(poNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order", "poNumber", poNumber));
        return mapToDTO(po);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderResponseDTO> searchPurchaseOrders(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllPurchaseOrders();
        }
        return purchaseOrderRepository.findByPoNumberContainingIgnoreCase(query.trim())
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private PurchaseOrderResponseDTO mapToDTO(PurchaseOrder po) {
        PurchaseOrderResponseDTO dto = new PurchaseOrderResponseDTO();
        dto.setId(po.getId());
        dto.setPoNumber(po.getPoNumber());
        dto.setSupplierName(po.getSupplierName());
        dto.setMaterialName(po.getMaterialName());
        dto.setOrderedQuantity(po.getOrderedQuantity());
        dto.setUnit(po.getUnit());
        dto.setTolerancePercentage(po.getTolerancePercentage());
        dto.setStatus(po.getStatus());
        dto.setCreatedAt(po.getCreatedAt());

        // Calculate min and max allowed inclusive boundaries
        dto.setMinAllowedQuantity(ToleranceCalculator.calculateMinAllowed(po.getOrderedQuantity(), po.getTolerancePercentage()));
        dto.setMaxAllowedQuantity(ToleranceCalculator.calculateMaxAllowed(po.getOrderedQuantity(), po.getTolerancePercentage()));

        return dto;
    }
}

