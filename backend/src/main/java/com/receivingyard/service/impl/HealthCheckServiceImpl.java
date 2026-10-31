package com.receivingyard.service.impl;

import com.receivingyard.dto.HealthStatusDTO;
import com.receivingyard.service.HealthCheckService;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class HealthCheckServiceImpl implements HealthCheckService {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckServiceImpl.class);

    private final DataSource dataSource;
    private final Flyway flyway;

    @Value("${spring.application.name:receiving-yard-backend}")
    private String applicationName;

    @Value("${app.receiving.default-tolerance-percent:5.0}")
    private double defaultTolerancePercent;

    @Autowired
    public HealthCheckServiceImpl(DataSource dataSource, @Autowired(required = false) Flyway flyway) {
        this.dataSource = dataSource;
        this.flyway = flyway;
    }

    @Override
    public HealthStatusDTO getSystemHealth() {
        HealthStatusDTO health = new HealthStatusDTO();
        health.setApplicationName(applicationName);
        health.setTimestamp(Instant.now());
        health.setUptimeSeconds(ManagementFactory.getRuntimeMXBean().getUptime() / 1000);

        // Check PostgreSQL Database Connection
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            health.setDatabaseConnected(true);
            health.setDatabaseProductName(metaData.getDatabaseProductName());
            health.setDatabaseVersion(metaData.getDatabaseProductVersion());
            health.setStatus("UP");
        } catch (Exception ex) {
            log.error("Database health check failed", ex);
            health.setDatabaseConnected(false);
            health.setDatabaseProductName("PostgreSQL (Unavailable)");
            health.setDatabaseVersion("N/A: " + ex.getMessage());
            health.setStatus("DOWN");
        }

        // Check Flyway Current Migration Version
        if (flyway != null) {
            try {
                MigrationInfo current = flyway.info().current();
                if (current != null) {
                    health.setFlywayMigrationVersion(current.getVersion() != null 
                            ? current.getVersion().getVersion() + " (" + current.getDescription() + ")"
                            : "Baseline");
                } else {
                    health.setFlywayMigrationVersion("No migrations applied yet");
                }
            } catch (Exception ex) {
                log.warn("Flyway info retrieval warning: {}", ex.getMessage());
                health.setFlywayMigrationVersion("Error reading Flyway state");
            }
        } else {
            health.setFlywayMigrationVersion("Flyway disabled / not present");
        }

        Map<String, Object> env = new HashMap<>();
        env.put("defaultTolerancePercent", defaultTolerancePercent);
        env.put("javaVersion", System.getProperty("java.version"));
        env.put("osName", System.getProperty("os.name"));
        health.setEnvironmentInfo(env);

        return health;
    }
}

