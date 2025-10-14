package com.top.jarvised;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;

import com.top.jarvised.Entities.School;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class MultiSchoolConfig {

    /**
     * Creates the master datasource first for loading school metadata
     */
    @Bean(name = "masterDataSource")
    public DataSource masterDataSource() {
        return createDefaultDataSource();
    }

    /**
     * Creates the routing datasource with all existing tenants loaded
     */
    @Bean(name = "dataSource")
    @DependsOn("masterDataSource")
    public DataSource dataSource(DataSource masterDataSource) {
        Map<Object, Object> resolvedDataSources = new HashMap<>();

        // Load all existing schools from the master database
        JdbcTemplate jdbcTemplate = new JdbcTemplate(masterDataSource);
        List<School> schools = jdbcTemplate.query(
            "SELECT id, school_name, db_url, db_username, db_password FROM schools",
            (rs, rowNum) -> {
                School school = new School();
                school.setId(rs.getLong("id"));
                school.setSchoolName(rs.getString("school_name"));
                school.setDbUrl(rs.getString("db_url"));
                school.setDbUsername(rs.getString("db_username"));
                school.setDbPassword(rs.getString("db_password"));
                return school;
            }
        );

        // Register each school's datasource
        for (School school : schools) {
            DataSource ds = createDataSource(school);
            resolvedDataSources.put(school.getId().toString(), ds);
        }

        DataSourceRouter router = new DataSourceRouter();
        router.setTargetDataSources(resolvedDataSources);
        router.setDefaultTargetDataSource(masterDataSource);
        router.afterPropertiesSet();
        return router;
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

