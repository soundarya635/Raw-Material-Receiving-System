package com.receivingyard.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PurchaseOrderRequestDTO {

    @NotBlank(message = "PO Number is required")
    private String poNumber;

    @NotBlank(message = "Supplier Name is required")
    private String supplierName;

    @NotBlank(message = "Material Name is required")
    private String materialName;

    @NotNull(message = "Ordered Quantity is required")
    @DecimalMin(value = "0.001", message = "Ordered Quantity must be greater than zero")
    private BigDecimal orderedQuantity;

    @NotBlank(message = "Unit is required")
    private String unit;

    private BigDecimal tolerancePercentage = BigDecimal.valueOf(5.00);

    public PurchaseOrderRequestDTO() {}

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
}

