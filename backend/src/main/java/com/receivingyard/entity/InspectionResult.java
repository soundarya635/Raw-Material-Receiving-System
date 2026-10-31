package com.receivingyard.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "inspection_results")
public class InspectionResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspection_id", nullable = false)
    @JsonBackReference
    private QualityInspection inspection;

    @Column(name = "test_parameter", nullable = false, length = 150)
    private String testParameter;

    @Column(name = "min_value", nullable = false, precision = 15, scale = 4)
    private BigDecimal minValue;

    @Column(name = "max_value", nullable = false, precision = 15, scale = 4)
    private BigDecimal maxValue;

    @Column(name = "actual_value", nullable = false, precision = 15, scale = 4)
    private BigDecimal actualValue;

    @Column(nullable = false, length = 50)
    private String unit;

    @Column(name = "is_pass", nullable = false)
    private boolean pass;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public InspectionResult() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public QualityInspection getInspection() { return inspection; }
    public void setInspection(QualityInspection inspection) { this.inspection = inspection; }

    public String getTestParameter() { return testParameter; }
    public void setTestParameter(String testParameter) { this.testParameter = testParameter; }

    public BigDecimal getMinValue() { return minValue; }
    public void setMinValue(BigDecimal minValue) { this.minValue = minValue; }

    public BigDecimal getMaxValue() { return maxValue; }
    public void setMaxValue(BigDecimal maxValue) { this.maxValue = maxValue; }

    public BigDecimal getActualValue() { return actualValue; }
    public void setActualValue(BigDecimal actualValue) { this.actualValue = actualValue; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public boolean isPass() { return pass; }
    public void setPass(boolean pass) { this.pass = pass; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}

