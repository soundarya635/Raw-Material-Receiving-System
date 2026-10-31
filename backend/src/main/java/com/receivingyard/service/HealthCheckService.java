package com.receivingyard.service;

import com.receivingyard.dto.HealthStatusDTO;

public interface HealthCheckService {
    HealthStatusDTO getSystemHealth();
}

