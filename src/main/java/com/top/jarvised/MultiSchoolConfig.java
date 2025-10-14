package com.top.jarvised;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class MultiSchoolConfig {

    /**
     * Creates the routing datasource - initially empty, defaults to master DB
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        Map<Object, Object> resolvedDataSources = new HashMap<>();

        DataSourceRouter router = new DataSourceRouter();
        router.setTargetDataSources(resolvedDataSources);
        router.setDefaultTargetDataSource(createDefaultDataSource());
        router.afterPropertiesSet();
        return router;
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

