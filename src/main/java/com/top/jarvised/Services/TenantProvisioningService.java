package com.top.jarvised.Services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.top.jarvised.DataSourceRouter;
import com.top.jarvised.Entities.School;
import com.top.jarvised.Repositories.SchoolRepository;
import com.zaxxer.hikari.HikariDataSource;

@Service
public class TenantProvisioningService {

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private DataSource dataSource;

    @Value("${spring.datasource.url}")
    private String masterDbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private int maxPoolSize;

    @Value("${spring.datasource.hikari.minimum-idle}")
    private int minIdle;

    /**
     * Creates a new tenant (school) with its own database
     * @param schoolName Unique school identifier
     * @return The created School entity
     */
    public School createTenant(String schoolName) {
        // Sanitize school name for database usage
        String sanitizedName = sanitizeDatabaseName(schoolName);
        String dbName = "school_" + sanitizedName;

        // Create the database
        createDatabase(dbName);

        // Build JDBC URL - extract base URL from master URL
        String baseUrl = masterDbUrl.substring(0, masterDbUrl.lastIndexOf('/'));
        String jdbcUrl = baseUrl + "/" + dbName;

        // Save school to master database
        School school = new School(schoolName, jdbcUrl, dbUsername, dbPassword);
        school = schoolRepository.save(school);

        // Register the new datasource with the router
        DataSource newDataSource = createDataSource(jdbcUrl, dbUsername, dbPassword);
        if (dataSource instanceof DataSourceRouter) {
            ((DataSourceRouter) dataSource).addDataSource(school.getId().toString(), newDataSource);
        }

        // Initialize schema for the new tenant database
        initializeTenantSchema(jdbcUrl, dbUsername, dbPassword);

        return school;
    }

    /**
     * Creates a MySQL database for the tenant
     */
    private void createDatabase(String dbName) {
        // Extract base URL without database name
        String baseUrl = masterDbUrl.substring(0, masterDbUrl.lastIndexOf('/')) + "/";

        try (Connection conn = DriverManager.getConnection(baseUrl, dbUsername, dbPassword);
             Statement stmt = conn.createStatement()) {

            // Create database if it doesn't exist
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName +
                             " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");

        } catch (Exception e) {
            throw new RuntimeException("Failed to create database for tenant: " + dbName, e);
        }
    }

    /**
     * Initialize the schema for a new tenant database
     * This will create all necessary tables via Hibernate auto-create
     */
    private void initializeTenantSchema(String jdbcUrl, String username, String password) {
        // Note: With spring.jpa.hibernate.ddl-auto=update, tables will be created
        // automatically when the datasource is first accessed
        // You could also run migration scripts here if needed
    }

    /**
     * Creates a HikariCP datasource for a tenant
     */
    private DataSource createDataSource(String jdbcUrl, String username, String password) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(jdbcUrl);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setMaximumPoolSize(maxPoolSize);
        ds.setMinimumIdle(minIdle);
        return ds;
    }

    /**
     * Sanitizes school name to be database-safe
     */
    private String sanitizeDatabaseName(String schoolName) {
        // Remove special characters and replace spaces with underscores
        return schoolName.toLowerCase()
                        .replaceAll("[^a-z0-9_]", "")
                        .replaceAll("\\s+", "_")
                        .substring(0, Math.min(schoolName.length(), 50));
    }
}
