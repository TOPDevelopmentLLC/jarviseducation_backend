package com.top.jarvised.Config;

import com.top.jarvised.Services.TenantProvisioningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Runs database migrations automatically on application startup.
 * This ensures all tenant databases are up-to-date with the latest schema.
 */
@Component
public class MigrationRunner implements ApplicationRunner {

    private final TenantProvisioningService tenantProvisioningService;

    @Autowired
    public MigrationRunner(TenantProvisioningService tenantProvisioningService) {
        this.tenantProvisioningService = tenantProvisioningService;
    }

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("Running tenant database migrations...");
        try {
            tenantProvisioningService.migrateAllTenantDatabases();
            System.out.println("Tenant database migrations completed successfully.");
        } catch (Exception e) {
            System.err.println("Error running tenant database migrations: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
