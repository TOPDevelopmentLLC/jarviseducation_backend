package com.top.jarvised;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DataSourceRouter extends AbstractRoutingDataSource {

    private Map<Object, Object> targetDataSources;

    @Override
    protected Object determineCurrentLookupKey() {
        String key = SchoolContext.getSchool();
        System.out.println("[DEBUG] DataSourceRouter.determineCurrentLookupKey: " + key + ", available keys: " + (targetDataSources != null ? targetDataSources.keySet() : "null"));
        return key;
    }

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        this.targetDataSources = targetDataSources;
    }

    /**
     * Dynamically adds a new datasource to the router at runtime
     * @param key The school ID key
     * @param dataSource The datasource to add
     */
    public void addDataSource(String key, DataSource dataSource) {
        if (this.targetDataSources != null) {
            this.targetDataSources.put(key, dataSource);
            super.setTargetDataSources(this.targetDataSources);
            super.afterPropertiesSet();
        }
    }
}

