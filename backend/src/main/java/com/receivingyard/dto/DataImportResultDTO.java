package com.receivingyard.dto;

import java.util.ArrayList;
import java.util.List;

public class DataImportResultDTO {
    private String fileName;
    private String fileType;
    private int totalRecords;
    private int successfulRecords;
    private int failedRecords;
    private List<String> messages = new ArrayList<>();
    private List<String> errorDetails = new ArrayList<>();
    private Object extractedData;

    public DataImportResultDTO() {}

    public DataImportResultDTO(String fileName, String fileType) {
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public int getTotalRecords() { return totalRecords; }
    public void setTotalRecords(int totalRecords) { this.totalRecords = totalRecords; }

    public int getSuccessfulRecords() { return successfulRecords; }
    public void setSuccessfulRecords(int successfulRecords) { this.successfulRecords = successfulRecords; }

    public int getFailedRecords() { return failedRecords; }
    public void setFailedRecords(int failedRecords) { this.failedRecords = failedRecords; }

    public List<String> getMessages() { return messages; }
    public void setMessages(List<String> messages) { this.messages = messages; }

    public List<String> getErrorDetails() { return errorDetails; }
    public void setErrorDetails(List<String> errorDetails) { this.errorDetails = errorDetails; }

    public Object getExtractedData() { return extractedData; }
    public void setExtractedData(Object extractedData) { this.extractedData = extractedData; }
}

