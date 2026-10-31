package com.receivingyard.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "po_number", nullable = false, unique = true, length = 100)
    private String poNumber;

    @Column(name = "supplier_name", nullable = false)
    private String supplierName;

    @Column(name = "material_name", nullable = false)
    private String materialName;

    @Column(name = "ordered_quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal orderedQuantity;

    @Column(nullable = false, length = 20)
    private String unit;

    @Column(name = "tolerance_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal tolerancePercentage = BigDecimal.valueOf(5.00);

    @Column(nullable = false, length = 50)
    private String status = "OPEN";

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public PurchaseOrder() {}

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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}

