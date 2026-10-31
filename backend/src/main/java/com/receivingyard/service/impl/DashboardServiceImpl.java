package com.receivingyard.service.impl;

import com.receivingyard.dto.DashboardSummaryDTO;
import com.receivingyard.entity.GoodsReceipt;
import com.receivingyard.repository.GoodsReceiptRepository;
import com.receivingyard.repository.InventoryRepository;
import com.receivingyard.repository.LotRepository;
import com.receivingyard.repository.PurchaseOrderRepository;
import com.receivingyard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final GoodsReceiptRepository goodsReceiptRepository;
    private final InventoryRepository inventoryRepository;
    private final LotRepository lotRepository;

    @Autowired
    public DashboardServiceImpl(PurchaseOrderRepository purchaseOrderRepository,
                                GoodsReceiptRepository goodsReceiptRepository,
                                InventoryRepository inventoryRepository,
                                LotRepository lotRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.goodsReceiptRepository = goodsReceiptRepository;
        this.inventoryRepository = inventoryRepository;
        this.lotRepository = lotRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardSummaryDTO getDashboardSummary() {
        DashboardSummaryDTO summary = new DashboardSummaryDTO();

        // 1. Total POs & Receipts
        summary.setTotalPurchaseOrders(purchaseOrderRepository.count());
        summary.setTotalGoodsReceipts(goodsReceiptRepository.count());

        // 2. Real Aggregated Quantities from PostgreSQL
        BigDecimal totalReceived = inventoryRepository.sumReceivedQuantity();
        BigDecimal quarantined = inventoryRepository.sumPendingQuantity();
        BigDecimal approved = inventoryRepository.sumAcceptedQuantity();
        BigDecimal rejected = inventoryRepository.sumRejectedQuantity();

        summary.setTotalReceivedQuantity(totalReceived != null ? totalReceived : BigDecimal.ZERO);
        summary.setQuarantinedQuantity(quarantined != null ? quarantined : BigDecimal.ZERO);
        summary.setApprovedQuantity(approved != null ? approved : BigDecimal.ZERO);
        summary.setRejectedQuantity(rejected != null ? rejected : BigDecimal.ZERO);

        // 3. Pending Inspections (lots in QUARANTINED or IN_INSPECTION)
        long pendingInspections = lotRepository.findAll().stream()
                .filter(l -> "QUARANTINED".equalsIgnoreCase(l.getStatus()) || "IN_INSPECTION".equalsIgnoreCase(l.getStatus()))
                .count();
        summary.setPendingInspections(pendingInspections);

        // 4. Recent Receipts
        List<GoodsReceipt> recentReceipts = goodsReceiptRepository.findTop10ByOrderByCreatedAtDesc();
        summary.setRecentReceipts(recentReceipts);

        return summary;
    }
}

