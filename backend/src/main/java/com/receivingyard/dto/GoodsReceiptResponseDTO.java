package com.receivingyard.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;

public class GoodsReceiptResponseDTO {
    private Long id;
    private String grnNumber;
    private String poNumber;
    private String supplier;
    private String material;
    private String truckNumber;
    private BigDecimal receivedQuantity;
    private String unit;
    private LocalDate arrivalDate;
    private boolean withinTolerance;
    private String discrepancyReason;
    private String lotNumber;
    private Instant createdAt;

    public GoodsReceiptResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getGrnNumber() { return grnNumber; }
    public void setGrnNumber(String grnNumber) { this.grnNumber = grnNumber; }

    public String getPoNumber() { return poNumber; }
    public void setPoNumber(String poNumber) { this.poNumber = poNumber; }

    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public String getTruckNumber() { return truckNumber; }
    public void setTruckNumber(String truckNumber) { this.truckNumber = truckNumber; }

    public BigDecimal getReceivedQuantity() { return receivedQuantity; }
    public void setReceivedQuantity(BigDecimal receivedQuantity) { this.receivedQuantity = receivedQuantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public LocalDate getArrivalDate() { return arrivalDate; }
    public void setArrivalDate(LocalDate arrivalDate) { this.arrivalDate = arrivalDate; }

    public boolean isWithinTolerance() { return withinTolerance; }
    public void setWithinTolerance(boolean withinTolerance) { this.withinTolerance = withinTolerance; }

    public String getDiscrepancyReason() { return discrepancyReason; }
    public void setDiscrepancyReason(String discrepancyReason) { this.discrepancyReason = discrepancyReason; }

    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}

