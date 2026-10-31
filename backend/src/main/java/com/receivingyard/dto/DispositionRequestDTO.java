package com.receivingyard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class DispositionRequestDTO {

    @NotBlank(message = "Lot Number is required")
    private String lotNumber;

    @NotBlank(message = "Decision is required (APPROVE_FULL, APPROVE_PARTIAL, REJECT_FULL)")
    private String decision;

    @NotNull(message = "Accepted Quantity is required")
    private BigDecimal acceptedQuantity;

    @NotNull(message = "Rejected Quantity is required")
    private BigDecimal rejectedQuantity;

    private String notes;

    public DispositionRequestDTO() {}

    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }

    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }

    public BigDecimal getAcceptedQuantity() { return acceptedQuantity; }
    public void setAcceptedQuantity(BigDecimal acceptedQuantity) { this.acceptedQuantity = acceptedQuantity; }

    public BigDecimal getRejectedQuantity() { return rejectedQuantity; }
    public void setRejectedQuantity(BigDecimal rejectedQuantity) { this.rejectedQuantity = rejectedQuantity; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}

