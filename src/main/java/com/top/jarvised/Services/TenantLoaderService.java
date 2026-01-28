package com.top.jarvised.Services;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.top.jarvised.DataSourceRouter;
import com.top.jarvised.SchoolContext;
import com.top.jarvised.Entities.School;
import com.top.jarvised.Repositories.SchoolRepository;
import com.zaxxer.hikari.HikariDataSource;

@Service
public class TenantLoaderService {

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private DataSource dataSource;

    /**
     * Load all existing tenants after application is fully initialized
     * This runs AFTER all beans are created, avoiding circular dependencies
     */
    @EventListener(ApplicationReadyEvent.class)
    public void loadExistingTenants() {
        try {
            // Clear school context to ensure we query from master DB
            SchoolContext.clear();

            List<School> schools = schoolRepository.findAll();

            if (dataSource instanceof DataSourceRouter) {
                DataSourceRouter router = (DataSourceRouter) dataSource;

                for (School school : schools) {
                    DataSource ds = createDataSource(school);
                    String tenantKey = school.getId().toString();
                    router.addDataSource(tenantKey, ds);
                    System.out.println("  [DEBUG] Registered tenant with key: " + tenantKey + " for school: " + school.getSchoolName());

                    // Run schema migration for existing tenants
                    migrateSchemaIfNeeded(ds, school.getSchoolName());
                }

                System.out.println("✓ Loaded " + schools.size() + " existing tenant(s)");
            }
        } catch (Exception e) {
            System.err.println("✗ Error loading existing tenants: " + e.getMessage());
            e.printStackTrace();
            // Continue startup even if loading fails
        }
    }

    /**
     * Creates a datasource for a specific school
     */
    private DataSource createDataSource(School school) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(school.getDbUrl());
        ds.setUsername(school.getDbUsername());
        ds.setPassword(school.getDbPassword());
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setMaximumPoolSize(10);
        ds.setMinimumIdle(2);
        return ds;
    }

    /**
     * Runs schema migration for existing tenant databases.
     * Uses CREATE TABLE IF NOT EXISTS so it's safe to run multiple times.
     */
    private void migrateSchemaIfNeeded(DataSource ds, String schoolName) {
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

        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(createSchoolYearSettingsTable);
            stmt.executeUpdate(createTermsTable);
            stmt.executeUpdate(createHolidaysTable);
            stmt.executeUpdate(createBreakPeriodsTable);
            stmt.executeUpdate(createSchedulePeriodsTable);

            System.out.println("  ✓ Schema migration complete for: " + schoolName);

        } catch (Exception e) {
            System.err.println("  ✗ Schema migration failed for " + schoolName + ": " + e.getMessage());
        }
    }
}
