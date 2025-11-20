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
        // Create SQL statements for all tables
        String createAdminsTable = """
            CREATE TABLE IF NOT EXISTS admins (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255)
            )
            """;

        String createParentsTable = """
            CREATE TABLE IF NOT EXISTS parents (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255)
            )
            """;

        String createStudentsTable = """
            CREATE TABLE IF NOT EXISTS students (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255),
                grade VARCHAR(50)
            )
            """;

        String createTeachersTable = """
            CREATE TABLE IF NOT EXISTS teachers (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255),
                subject VARCHAR(255)
            )
            """;

        String createClassCatalogueTable = """
            CREATE TABLE IF NOT EXISTS courses (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                course_name VARCHAR(255),
                course_description TEXT
            )
            """;

        String createReportsTable = """
            CREATE TABLE IF NOT EXISTS reports (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                report_type VARCHAR(50),
                description TEXT,
                reported_by_name VARCHAR(255),
                reported_by_id BIGINT,
                mood_type VARCHAR(50),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;

        String createCommentsTable = """
            CREATE TABLE IF NOT EXISTS comments (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                body_text TEXT,
                posted_by_name VARCHAR(255),
                posted_by_id BIGINT,
                report_id BIGINT,
                is_edited BOOLEAN DEFAULT FALSE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                last_edited_timestamp TIMESTAMP NULL,
                FOREIGN KEY (report_id) REFERENCES reports(id)
            )
            """;

        String createTeamsTable = """
            CREATE TABLE IF NOT EXISTS teams (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                team_name VARCHAR(255),
                team_lead_id BIGINT
            )
            """;

        String createUserAccountTeamsTable = """
            CREATE TABLE IF NOT EXISTS user_account_teams (
                user_account_id BIGINT,
                team_id BIGINT,
                PRIMARY KEY (user_account_id, team_id)
            )
            """;

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             Statement stmt = conn.createStatement()) {

            // Execute all CREATE TABLE statements
            stmt.executeUpdate(createAdminsTable);
            stmt.executeUpdate(createParentsTable);
            stmt.executeUpdate(createStudentsTable);
            stmt.executeUpdate(createTeachersTable);
            stmt.executeUpdate(createClassCatalogueTable);
            stmt.executeUpdate(createReportsTable);
            stmt.executeUpdate(createCommentsTable);
            stmt.executeUpdate(createTeamsTable);
            stmt.executeUpdate(createUserAccountTeamsTable);

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize schema for tenant database: " + jdbcUrl, e);
        }
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
        String sanitized = schoolName.toLowerCase()
                        .replaceAll("[^a-z0-9_]", "")
                        .replaceAll("\\s+", "_");

        // Limit to 50 characters using the sanitized length
        return sanitized.substring(0, Math.min(sanitized.length(), 50));
    }

    /**
     * DEVELOPMENT ONLY: Drops a tenant database
     * WARNING: This is a destructive operation
     */
    public void dropTenantDatabase(School school) {
        // Extract database name from JDBC URL
        String jdbcUrl = school.getDbUrl();
        String dbName = jdbcUrl.substring(jdbcUrl.lastIndexOf('/') + 1);

        // Extract base URL without database name
        String baseUrl = masterDbUrl.substring(0, masterDbUrl.lastIndexOf('/')) + "/";

        try (Connection conn = DriverManager.getConnection(baseUrl, dbUsername, dbPassword);
             Statement stmt = conn.createStatement()) {

            // Drop the database
            stmt.executeUpdate("DROP DATABASE IF EXISTS " + dbName);

        } catch (Exception e) {
            throw new RuntimeException("Failed to drop database for tenant: " + dbName, e);
        }
    }
}
