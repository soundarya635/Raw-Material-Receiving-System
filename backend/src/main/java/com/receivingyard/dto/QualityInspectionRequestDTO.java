package com.receivingyard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class QualityInspectionRequestDTO {

    @NotBlank(message = "Lot Number is required")
    private String lotNumber;

    private String inspectorName = "Quality Inspector";
    private String notes;

    @NotEmpty(message = "At least one test parameter is required")
    private List<TestParameterDTO> tests;

    public QualityInspectionRequestDTO() {}

    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }

    public String getInspectorName() { return inspectorName; }
    public void setInspectorName(String inspectorName) { this.inspectorName = inspectorName; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public List<TestParameterDTO> getTests() { return tests; }
    public void setTests(List<TestParameterDTO> tests) { this.tests = tests; }
}

