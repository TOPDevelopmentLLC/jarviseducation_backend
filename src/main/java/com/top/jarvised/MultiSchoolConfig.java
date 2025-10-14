package com.top.jarvised;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;

import com.top.jarvised.Entities.School;
import com.top.jarvised.Repositories.SchoolRepository;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class MultiSchoolConfig {

    private DataSourceRouter routerInstance;

    @Autowired
    private SchoolRepository schoolRepository;

    /**
     * Creates the routing datasource - initially empty
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        Map<Object, Object> resolvedDataSources = new HashMap<>();

        routerInstance = new DataSourceRouter();
        routerInstance.setTargetDataSources(resolvedDataSources);
        routerInstance.setDefaultTargetDataSource(createDefaultDataSource());
        routerInstance.afterPropertiesSet();
        return routerInstance;
    }

    /**
     * Load all existing tenants after application is fully initialized
     */
    @EventListener(ApplicationReadyEvent.class)
    public void loadExistingTenants() {
        try {
            // Clear school context to ensure we query from master DB
            SchoolContext.clear();

            List<School> schools = schoolRepository.findAll();

            for (School school : schools) {
                DataSource ds = createDataSource(school);
                if (routerInstance != null) {
                    routerInstance.addDataSource(school.getId().toString(), ds);
                }
            }

            System.out.println("Loaded " + schools.size() + " existing tenant(s)");
        } catch (Exception e) {
            System.err.println("Error loading existing tenants: " + e.getMessage());
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
     * Creates the default master database datasource
     */
    private DataSource createDefaultDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:mysql://localhost:3306/master_db");
        ds.setUsername("root");
        ds.setPassword("secret");
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return ds;
    }
}

