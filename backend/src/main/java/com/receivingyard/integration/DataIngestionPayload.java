package com.receivingyard.integration;

import com.receivingyard.enums.SourceType;

import java.time.Instant;
import java.util.Map;

/**
 * Common Canonical Ingestion Payload representing an extracted record from
 * any source (Excel, CSV, PDF, Google Sheets, ERP API) before validation and persistence.
 */
public class DataIngestionPayload {
    private String sourceIdentifier;
    private SourceType sourceType;
    private String payloadChecksum;
    private String rawContentType;
    private Map<String, Object> attributes;
    private Instant capturedAt;

    public DataIngestionPayload() {
        this.capturedAt = Instant.now();
    }

    public DataIngestionPayload(String sourceIdentifier, SourceType sourceType, Map<String, Object> attributes) {
        this.sourceIdentifier = sourceIdentifier;
        this.sourceType = sourceType;
        this.attributes = attributes;
        this.capturedAt = Instant.now();
    }

    public String getSourceIdentifier() {
        return sourceIdentifier;
    }

    public void setSourceIdentifier(String sourceIdentifier) {
        this.sourceIdentifier = sourceIdentifier;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getPayloadChecksum() {
        return payloadChecksum;
    }

    public void setPayloadChecksum(String payloadChecksum) {
        this.payloadChecksum = payloadChecksum;
    }

    public String getRawContentType() {
        return rawContentType;
    }

    public void setRawContentType(String rawContentType) {
        this.rawContentType = rawContentType;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Instant getCapturedAt() {
        return capturedAt;
    }

    public void setCapturedAt(Instant capturedAt) {
        this.capturedAt = capturedAt;
    }
}

