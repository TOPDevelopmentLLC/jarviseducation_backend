package com.top.jarvised;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.top.jarvised.Entities.School;
import com.top.jarvised.Repositories.SchoolRepository;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class MultiSchoolConfig {

    @Bean
    public DataSource dataSource() {
        Map<Object, Object> resolvedDataSources = new HashMap<>();

        // Example: dynamically populate from a config, database, etc.
        resolvedDataSources.put("jarvisprep", createDataSource("jdbc:mysql://.../jarvisprep"));
        resolvedDataSources.put("east_high", createDataSource("jdbc:mysql://.../east_high"));

        // List<School> schools = schoolRepository.findAll();
        // for (School school : schools) {
        //     DataSource ds = createDataSource(school);
        //     resolvedDataSources.put(school.getId(), ds);
        // }

        DataSourceRouter router = new DataSourceRouter();
        router.setTargetDataSources(resolvedDataSources);
        router.setDefaultTargetDataSource(createDefaultDataSource());
        router.afterPropertiesSet();
        return router;
    }

    private DataSource createDataSource(School school) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(school.getDbUrl());
        ds.setUsername("root");
        ds.setPassword("password");
        return ds;
    }

    private DataSource createDataSource(String url) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setUsername("root");
        ds.setPassword("password");
        return ds;
    }

    private DataSource createDefaultDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:mysql://localhost:3306/master_db");
        ds.setUsername("root");
        ds.setPassword("secret");
        return ds;
    }

}

