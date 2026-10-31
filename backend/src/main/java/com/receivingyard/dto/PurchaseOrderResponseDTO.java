package com.receivingyard.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class PurchaseOrderResponseDTO {
    private Long id;
    private String poNumber;
    private String supplierName;
    private String materialName;
    private BigDecimal orderedQuantity;
    private String unit;
    private BigDecimal tolerancePercentage;
    private BigDecimal minAllowedQuantity;
    private BigDecimal maxAllowedQuantity;
    private String status;
    private Instant createdAt;

    public PurchaseOrderResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPoNumber() { return poNumber; }
    public void setPoNumber(String poNumber) { this.poNumber = poNumber; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }

    public BigDecimal getOrderedQuantity() { return orderedQuantity; }
    public void setOrderedQuantity(BigDecimal orderedQuantity) { this.orderedQuantity = orderedQuantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public BigDecimal getTolerancePercentage() { return tolerancePercentage; }
    public void setTolerancePercentage(BigDecimal tolerancePercentage) { this.tolerancePercentage = tolerancePercentage; }

    public BigDecimal getMinAllowedQuantity() { return minAllowedQuantity; }
    public void setMinAllowedQuantity(BigDecimal minAllowedQuantity) { this.minAllowedQuantity = minAllowedQuantity; }

    public BigDecimal getMaxAllowedQuantity() { return maxAllowedQuantity; }
    public void setMaxAllowedQuantity(BigDecimal maxAllowedQuantity) { this.maxAllowedQuantity = maxAllowedQuantity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}

