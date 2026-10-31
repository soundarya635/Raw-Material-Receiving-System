package com.receivingyard.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class GoodsReceiptRequestDTO {

    @NotBlank(message = "GRN Number is required")
    private String grnNumber;

    @NotBlank(message = "PO Number is required")
    private String poNumber;

    @NotBlank(message = "Truck Number is required")
    private String truckNumber;

    @NotNull(message = "Received Quantity is required")
    @DecimalMin(value = "0.001", message = "Received Quantity must be greater than zero")
    private BigDecimal receivedQuantity;

    private String unit;
    private LocalDate arrivalDate;

    public GoodsReceiptRequestDTO() {}

    public String getGrnNumber() { return grnNumber; }
    public void setGrnNumber(String grnNumber) { this.grnNumber = grnNumber; }

    public String getPoNumber() { return poNumber; }
    public void setPoNumber(String poNumber) { this.poNumber = poNumber; }

    public String getTruckNumber() { return truckNumber; }
    public void setTruckNumber(String truckNumber) { this.truckNumber = truckNumber; }

    public BigDecimal getReceivedQuantity() { return receivedQuantity; }
    public void setReceivedQuantity(BigDecimal receivedQuantity) { this.receivedQuantity = receivedQuantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public LocalDate getArrivalDate() { return arrivalDate; }
    public void setArrivalDate(LocalDate arrivalDate) { this.arrivalDate = arrivalDate; }
}

