package com.top.jarvised.Services;

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
                    router.addDataSource(school.getId().toString(), ds);
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
}
