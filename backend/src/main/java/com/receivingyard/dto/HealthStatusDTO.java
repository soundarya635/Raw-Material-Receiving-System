package com.receivingyard.dto;

import java.time.Instant;
import java.util.Map;

public class HealthStatusDTO {
    private String status;
    private String applicationName;
    private boolean databaseConnected;
    private String databaseProductName;
    private String databaseVersion;
    private String flywayMigrationVersion;
    private long uptimeSeconds;
    private Instant timestamp;
    private Map<String, Object> environmentInfo;

    public HealthStatusDTO() {
        this.timestamp = Instant.now();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public boolean isDatabaseConnected() {
        return databaseConnected;
    }

    public void setDatabaseConnected(boolean databaseConnected) {
        this.databaseConnected = databaseConnected;
    }

    public String getDatabaseProductName() {
        return databaseProductName;
    }

    public void setDatabaseProductName(String databaseProductName) {
        this.databaseProductName = databaseProductName;
    }

    public String getDatabaseVersion() {
        return databaseVersion;
    }

    public void setDatabaseVersion(String databaseVersion) {
        this.databaseVersion = databaseVersion;
    }

    public String getFlywayMigrationVersion() {
        return flywayMigrationVersion;
    }

    public void setFlywayMigrationVersion(String flywayMigrationVersion) {
        this.flywayMigrationVersion = flywayMigrationVersion;
    }

    public long getUptimeSeconds() {
        return uptimeSeconds;
    }

    public void setUptimeSeconds(long uptimeSeconds) {
        this.uptimeSeconds = uptimeSeconds;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getEnvironmentInfo() {
        return environmentInfo;
    }

    public void setEnvironmentInfo(Map<String, Object> environmentInfo) {
        this.environmentInfo = environmentInfo;
    }
}

