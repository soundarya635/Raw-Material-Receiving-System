package com.receivingyard.exception;

import java.util.Collections;
import java.util.List;

public class BusinessValidationException extends RuntimeException {
    private final List<String> errorDetails;

    public BusinessValidationException(String message) {
        super(message);
        this.errorDetails = Collections.emptyList();
    }

    public BusinessValidationException(String message, List<String> errorDetails) {
        super(message);
        this.errorDetails = errorDetails != null ? errorDetails : Collections.emptyList();
    }

    public List<String> getErrorDetails() {
        return errorDetails;
    }
}

