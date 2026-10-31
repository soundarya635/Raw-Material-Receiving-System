package com.receivingyard.dto;

import java.time.Instant;
import java.util.List;

public class QualityInspectionResponseDTO {
    private Long id;
    private String lotNumber;
    private String inspectorName;
    private Instant inspectionDate;
    private String overallResult;
    private String notes;
    private List<TestParameterDTO> results;
    private Instant createdAt;

    public QualityInspectionResponseDTO() {}

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

    public List<TestParameterDTO> getResults() { return results; }
    public void setResults(List<TestParameterDTO> results) { this.results = results; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}

