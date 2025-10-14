package com.top.jarvised.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.top.jarvised.JwtUtil;
import com.top.jarvised.SchoolContext;
import com.top.jarvised.Entities.School;
import com.top.jarvised.Entities.UserAccount;
import com.top.jarvised.Repositories.UserAccountRepository;

@Service
public class UserAccountService {

    private UserAccountRepository userAccountRepository;
    private JwtUtil jwtUtil;
    private TenantProvisioningService tenantProvisioningService;

    @Autowired
    public UserAccountService(
            UserAccountRepository userAccountRepository,
            JwtUtil jwtUtil,
            TenantProvisioningService tenantProvisioningService) {
        this.userAccountRepository = userAccountRepository;
        this.jwtUtil = jwtUtil;
        this.tenantProvisioningService = tenantProvisioningService;
    }

    /**
     * Authenticates user and generates JWT with schoolId claim
     */
    public UserAccount attemptLogin(String email, String password) {
        UserAccount user = userAccountRepository.findByEmailAndPassword(email, password)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }

        // Generate JWT with schoolId for tenant routing
        String token = jwtUtil.generateToken(user.getEmail(), user.getSchoolId());
        user.setToken(token);

        return user;
    }

    /**
     * Creates a new user account with automatic tenant provisioning
     * @param email User's email
     * @param password User's password
     * @param schoolName The school/tenant name to create
     * @return Created UserAccount with JWT token
     */
    @Transactional
    public UserAccount createAccount(String email, String password, String schoolName) {
        if (userAccountRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        // Create new tenant (school) with its own database
        School school = tenantProvisioningService.createTenant(schoolName);

        // Create user account associated with the new school
        UserAccount user = new UserAccount(email, password, school.getId());

        // Set school context for saving to master DB
        SchoolContext.clear();

        user = userAccountRepository.save(user);

        // Generate JWT with schoolId
        String token = jwtUtil.generateToken(user.getEmail(), user.getSchoolId());
        user.setToken(token);

        return user;
    }
}
