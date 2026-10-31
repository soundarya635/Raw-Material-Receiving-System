package com.receivingyard.controller;

import com.receivingyard.dto.ApiResponse;
import com.receivingyard.dto.HealthStatusDTO;
import com.receivingyard.service.HealthCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthCheckController {

    private final HealthCheckService healthCheckService;

    @Autowired
    public HealthCheckController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<HealthStatusDTO>> getHealth() {
        HealthStatusDTO health = healthCheckService.getSystemHealth();
        return ResponseEntity.ok(ApiResponse.ok("System health verified successfully", health));
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<String>> getStatus() {
        return ResponseEntity.ok(ApiResponse.ok("Raw Material Receiving Yard & QC System Backend is operational"));
    }
}

