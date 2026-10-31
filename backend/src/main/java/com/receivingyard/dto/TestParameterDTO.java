package com.receivingyard.dto;

import java.math.BigDecimal;

public class TestParameterDTO {
    private String testParameter;
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private BigDecimal actualValue;
    private String unit;
    private boolean pass;

    public TestParameterDTO() {}

    public TestParameterDTO(String testParameter, BigDecimal minValue, BigDecimal maxValue, BigDecimal actualValue, String unit) {
        this.testParameter = testParameter;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.actualValue = actualValue;
        this.unit = unit;
        // Inclusive evaluation: minValue <= actualValue <= maxValue
        if (minValue != null && maxValue != null && actualValue != null) {
            this.pass = actualValue.compareTo(minValue) >= 0 && actualValue.compareTo(maxValue) <= 0;
        } else {
            this.pass = false;
        }
    }

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
}

