package com.receivingyard.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quality_inspections")
public class QualityInspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lot_number", nullable = false, length = 100)
    private String lotNumber;

    @Column(name = "inspector_name", nullable = false)
    private String inspectorName = "Quality Inspector";

    @Column(name = "inspection_date", nullable = false)
    private Instant inspectionDate = Instant.now();

    @Column(name = "overall_result", nullable = false, length = 50)
    private String overallResult = "PENDING"; // PASS, FAIL, PENDING

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "inspection", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<InspectionResult> results = new ArrayList<>();

    public QualityInspection() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }

    public String getInspectorName() { return inspectorName; }
    public void setInspectorName(String inspectorName) { this.inspectorName = inspectorName; }

    public Instant getInspectionDate() { return inspectionDate; }
    public void setInspectionDate(Instant inspectionDate) { this.inspectionDate = inspectionDate; }

    public String getOverallResult() { return overallResult; }
    public void setOverallResult(String overallResult) { this.overallResult = overallResult; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public List<InspectionResult> getResults() { return results; }
    public void setResults(List<InspectionResult> results) { this.results = results; }

    public void addResult(InspectionResult result) {
        results.add(result);
        result.setInspection(this);
    }
}

