package com.receivingyard.integration;

import com.receivingyard.enums.SourceType;

import java.io.InputStream;
import java.util.List;

/**
 * Strategy interface defining source-independent data extraction.
 * New input formats (Excel, CSV, PDF, ERP APIs) implement this contract
 * without altering downstream core domain models or business logic.
 */
public interface SourceDataAdapter<T> {

    /**
     * Determines whether this adapter supports the given source type and mime format.
     */
    boolean supports(SourceType sourceType, String mimeType);

    /**
     * Extracts and normalizes input data into canonical Ingestion Payloads.
     */
    List<DataIngestionPayload> parse(InputStream inputStream, String sourceIdentifier) throws Exception;
}

