package com.top.jarvised.Services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

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
                grade VARCHAR(50),
                is_active BOOLEAN DEFAULT TRUE
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
                student_id BIGINT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (student_id) REFERENCES students(id)
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

        String createSchoolYearSettingsTable = """
            CREATE TABLE IF NOT EXISTS school_year_settings (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                start_date DATE,
                end_date DATE,
                term_type VARCHAR(50) NOT NULL,
                school_day_start TIME,
                school_day_end TIME,
                timezone VARCHAR(100) NOT NULL,
                is_active BOOLEAN DEFAULT FALSE,
                school_id BIGINT NOT NULL
            )
            """;

        String createTermsTable = """
            CREATE TABLE IF NOT EXISTS terms (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                term_number INT NOT NULL,
                start_date DATE NOT NULL,
                end_date DATE NOT NULL,
                school_year_settings_id BIGINT NOT NULL,
                FOREIGN KEY (school_year_settings_id) REFERENCES school_year_settings(id) ON DELETE CASCADE
            )
            """;

        String createHolidaysTable = """
            CREATE TABLE IF NOT EXISTS holidays (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                date DATE NOT NULL,
                description TEXT,
                school_year_settings_id BIGINT NOT NULL,
                FOREIGN KEY (school_year_settings_id) REFERENCES school_year_settings(id) ON DELETE CASCADE
            )
            """;

        String createBreakPeriodsTable = """
            CREATE TABLE IF NOT EXISTS break_periods (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                break_type VARCHAR(50) NOT NULL,
                start_date DATE NOT NULL,
                end_date DATE NOT NULL,
                description TEXT,
                school_year_settings_id BIGINT NOT NULL,
                FOREIGN KEY (school_year_settings_id) REFERENCES school_year_settings(id) ON DELETE CASCADE
            )
            """;

        String createSchedulePeriodsTable = """
            CREATE TABLE IF NOT EXISTS schedule_periods (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                period_number INT NOT NULL,
                period_type VARCHAR(50) NOT NULL,
                start_time TIME NOT NULL,
                end_time TIME NOT NULL,
                school_year_settings_id BIGINT NOT NULL,
                FOREIGN KEY (school_year_settings_id) REFERENCES school_year_settings(id) ON DELETE CASCADE
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
            stmt.executeUpdate(createSchoolYearSettingsTable);
            stmt.executeUpdate(createTermsTable);
            stmt.executeUpdate(createHolidaysTable);
            stmt.executeUpdate(createBreakPeriodsTable);
            stmt.executeUpdate(createSchedulePeriodsTable);

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
     * Migrates an existing tenant database to add new columns.
     * This is idempotent - safe to run multiple times.
     */
    public void migrateTenantDatabase(School school) {
        String jdbcUrl = school.getDbUrl();

        // Extract database name from JDBC URL for metadata queries
        String dbName = jdbcUrl.substring(jdbcUrl.lastIndexOf('/') + 1);
        // Remove any query parameters if present
        if (dbName.contains("?")) {
            dbName = dbName.substring(0, dbName.indexOf('?'));
        }

        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
             Statement stmt = conn.createStatement()) {

            System.out.println("Migrating database: " + dbName);

            // Check if is_active column exists in students table
            // Using catalog (database name) for proper MySQL metadata lookup
            ResultSet rs = conn.getMetaData().getColumns(dbName, null, "students", "is_active");
            boolean isActiveExists = rs.next();
            rs.close();

            if (!isActiveExists) {
                // Column doesn't exist, add it
                stmt.executeUpdate("ALTER TABLE students ADD COLUMN is_active BOOLEAN DEFAULT TRUE");
                System.out.println("Added is_active column to students table for " + dbName);
            } else {
                System.out.println("is_active column already exists in students table for " + dbName);
            }

            // Set all existing students to active (where is_active is null)
            int updated = stmt.executeUpdate("UPDATE students SET is_active = TRUE WHERE is_active IS NULL");
            System.out.println("Updated " + updated + " students with null is_active to TRUE");

            // Check if student_id column exists in reports table
            rs = conn.getMetaData().getColumns(dbName, null, "reports", "student_id");
            boolean studentIdExists = rs.next();
            rs.close();

            if (!studentIdExists) {
                // Column doesn't exist, add it
                stmt.executeUpdate("ALTER TABLE reports ADD COLUMN student_id BIGINT");
                System.out.println("Added student_id column to reports table for " + dbName);
            } else {
                System.out.println("student_id column already exists in reports table for " + dbName);
            }

            // Fix enum values that were stored as ordinals instead of names
            // ReportType enum order: ABC=0, Attendance=1, Behavior=2, Conflict=3, Expelled=4, Mood=5, Secluded=6, SIP=7, None=8
            String[] reportTypes = {"ABC", "Attendance", "Behavior", "Conflict", "Expelled", "Mood", "Secluded", "SIP", "None"};
            for (int i = 0; i < reportTypes.length; i++) {
                int fixedRows = stmt.executeUpdate("UPDATE reports SET report_type = '" + reportTypes[i] + "' WHERE report_type = '" + i + "'");
                if (fixedRows > 0) {
                    System.out.println("Fixed " + fixedRows + " reports with report_type ordinal " + i + " -> " + reportTypes[i]);
                }
            }

            // MoodType enum order: Green=0, Blue=1, Yellow=2, Red=3, None=4
            String[] moodTypes = {"Green", "Blue", "Yellow", "Red", "None"};
            for (int i = 0; i < moodTypes.length; i++) {
                int fixedRows = stmt.executeUpdate("UPDATE reports SET mood_type = '" + moodTypes[i] + "' WHERE mood_type = '" + i + "'");
                if (fixedRows > 0) {
                    System.out.println("Fixed " + fixedRows + " reports with mood_type ordinal " + i + " -> " + moodTypes[i]);
                }
            }

            System.out.println("Successfully migrated tenant database: " + dbName);

        } catch (Exception e) {
            System.err.println("Migration error for " + dbName + ": " + e.getClass().getName() + " - " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Caused by: " + e.getCause().getClass().getName() + " - " + e.getCause().getMessage());
            }
            throw new RuntimeException("Failed to migrate tenant database: " + jdbcUrl, e);
        }
    }

    /**
     * Migrates all existing tenant databases.
     * Call this on application startup or via an admin endpoint.
     */
    public void migrateAllTenantDatabases() {
        List<School> schools = schoolRepository.findAll();
        for (School school : schools) {
            try {
                migrateTenantDatabase(school);
            } catch (Exception e) {
                System.err.println("Failed to migrate database for school " + school.getId() + ": " + e.getMessage());
                // Print full stack trace for debugging
                e.printStackTrace();
            }
        }
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
