package com.receivingyard.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "inventory")
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lot_number", nullable = false, unique = true, length = 100)
    private String lotNumber;

    @Column(nullable = false)
    private String material;

    @Column(name = "received_quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal receivedQuantity;

    @Column(name = "accepted_quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal acceptedQuantity = BigDecimal.ZERO;

    @Column(name = "rejected_quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal rejectedQuantity = BigDecimal.ZERO;

    @Column(name = "pending_quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal pendingQuantity;

    @Column(name = "production_available_quantity", nullable = false, precision = 15, scale = 3)
    private BigDecimal productionAvailableQuantity = BigDecimal.ZERO;

    @Column(nullable = false, length = 20)
    private String unit;

    @Column(nullable = false, length = 50)
    private String status = "QUARANTINED";

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    public InventoryItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public BigDecimal getReceivedQuantity() { return receivedQuantity; }
    public void setReceivedQuantity(BigDecimal receivedQuantity) { this.receivedQuantity = receivedQuantity; }

    public BigDecimal getAcceptedQuantity() { return acceptedQuantity; }
    public void setAcceptedQuantity(BigDecimal acceptedQuantity) { this.acceptedQuantity = acceptedQuantity; }

    public BigDecimal getRejectedQuantity() { return rejectedQuantity; }
    public void setRejectedQuantity(BigDecimal rejectedQuantity) { this.rejectedQuantity = rejectedQuantity; }

    public BigDecimal getPendingQuantity() { return pendingQuantity; }
    public void setPendingQuantity(BigDecimal pendingQuantity) { this.pendingQuantity = pendingQuantity; }

    public BigDecimal getProductionAvailableQuantity() { return productionAvailableQuantity; }
    public void setProductionAvailableQuantity(BigDecimal productionAvailableQuantity) { this.productionAvailableQuantity = productionAvailableQuantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}

