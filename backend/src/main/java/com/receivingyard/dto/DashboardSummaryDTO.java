package com.receivingyard.dto;

import com.receivingyard.entity.GoodsReceipt;

import java.math.BigDecimal;
import java.util.List;

public class DashboardSummaryDTO {
    private long totalPurchaseOrders;
    private long totalGoodsReceipts;
    private BigDecimal totalReceivedQuantity;
    private BigDecimal quarantinedQuantity;
    private BigDecimal approvedQuantity;
    private BigDecimal rejectedQuantity;
    private long pendingInspections;
    private List<GoodsReceipt> recentReceipts;

    public DashboardSummaryDTO() {}

    public long getTotalPurchaseOrders() { return totalPurchaseOrders; }
    public void setTotalPurchaseOrders(long totalPurchaseOrders) { this.totalPurchaseOrders = totalPurchaseOrders; }

    public long getTotalGoodsReceipts() { return totalGoodsReceipts; }
    public void setTotalGoodsReceipts(long totalGoodsReceipts) { this.totalGoodsReceipts = totalGoodsReceipts; }

    public BigDecimal getTotalReceivedQuantity() { return totalReceivedQuantity; }
    public void setTotalReceivedQuantity(BigDecimal totalReceivedQuantity) { this.totalReceivedQuantity = totalReceivedQuantity; }

    public BigDecimal getQuarantinedQuantity() { return quarantinedQuantity; }
    public void setQuarantinedQuantity(BigDecimal quarantinedQuantity) { this.quarantinedQuantity = quarantinedQuantity; }

    public BigDecimal getApprovedQuantity() { return approvedQuantity; }
    public void setApprovedQuantity(BigDecimal approvedQuantity) { this.approvedQuantity = approvedQuantity; }

    public BigDecimal getRejectedQuantity() { return rejectedQuantity; }
    public void setRejectedQuantity(BigDecimal rejectedQuantity) { this.rejectedQuantity = rejectedQuantity; }

    public long getPendingInspections() { return pendingInspections; }
    public void setPendingInspections(long pendingInspections) { this.pendingInspections = pendingInspections; }

    public List<GoodsReceipt> getRecentReceipts() { return recentReceipts; }
    public void setRecentReceipts(List<GoodsReceipt> recentReceipts) { this.recentReceipts = recentReceipts; }
}

