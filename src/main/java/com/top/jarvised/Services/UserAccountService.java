package com.top.jarvised.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.top.jarvised.JwtUtil;
import com.top.jarvised.SchoolContext;
import com.top.jarvised.DTOs.CreateUserRequest;
import com.top.jarvised.DTOs.UserCreationResult;
import com.top.jarvised.Entities.School;
import com.top.jarvised.Entities.UserAccount;
import com.top.jarvised.Enums.AccountType;
import com.top.jarvised.Repositories.SchoolRepository;
import com.top.jarvised.Repositories.UserAccountRepository;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserAccountService {

    private UserAccountRepository userAccountRepository;
    private JwtUtil jwtUtil;
    private TenantProvisioningService tenantProvisioningService;
    private SchoolRepository schoolRepository;

    private static final String DEFAULT_PASSWORD = "TempPass123!";
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    public UserAccountService(
            UserAccountRepository userAccountRepository,
            JwtUtil jwtUtil,
            TenantProvisioningService tenantProvisioningService,
            SchoolRepository schoolRepository) {
        this.userAccountRepository = userAccountRepository;
        this.jwtUtil = jwtUtil;
        this.tenantProvisioningService = tenantProvisioningService;
        this.schoolRepository = schoolRepository;
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

    /**
     * Generates a random secure password
     * @param length Length of the password to generate
     * @return Random password string
     */
    private String generateRandomPassword(int length) {
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }

    /**
     * Converts string account type to AccountType enum
     * @param accountTypeStr String representation of account type
     * @return AccountType enum value
     */
    private AccountType parseAccountType(String accountTypeStr) {
        try {
            return AccountType.valueOf(accountTypeStr);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid account type: " + accountTypeStr);
        }
    }

    /**
     * Creates a single user for an existing tenant (admin only)
     * @param request User creation request with fullName, email, and accountType
     * @param adminSchoolId The school ID of the admin creating the user
     * @return UserCreationResult with success status and temporary password
     */
    @Transactional
    public UserCreationResult createUserForTenant(CreateUserRequest request, Long adminSchoolId) {
        try {
            // Validate email isn't already in use
            if (userAccountRepository.findByEmail(request.getEmail()).isPresent()) {
                return new UserCreationResult(false, request.getEmail(),
                    "Email already in use", null);
            }

            // Verify the school exists
            schoolRepository.findById(adminSchoolId)
                .orElseThrow(() -> new RuntimeException("School not found"));

            // Generate temporary password
            String temporaryPassword = generateRandomPassword(12);

            // Create user account using constructor
            UserAccount newUser = new UserAccount(
                request.getEmail(),
                temporaryPassword,
                parseAccountType(request.getAccountType()),
                adminSchoolId,
                request.getFullName()
            );

            // Clear context to ensure saving to master DB
            SchoolContext.clear();
            userAccountRepository.save(newUser);

            return new UserCreationResult(true, request.getEmail(),
                "User created successfully", temporaryPassword);

        } catch (Exception e) {
            return new UserCreationResult(false, request.getEmail(),
                "Error creating user: " + e.getMessage(), null);
        }
    }

    /**
     * Creates multiple users for an existing tenant in batch (admin only)
     * @param requests List of user creation requests
     * @param adminSchoolId The school ID of the admin creating the users
     * @return List of UserCreationResult for each user
     */
    @Transactional
    public List<UserCreationResult> createUsersForTenantBatch(
            List<CreateUserRequest> requests, Long adminSchoolId) {

        List<UserCreationResult> results = new ArrayList<>();

        // Verify the school exists once at the beginning
        schoolRepository.findById(adminSchoolId)
            .orElseThrow(() -> new RuntimeException("School not found"));

        for (CreateUserRequest request : requests) {
            results.add(createUserForTenant(request, adminSchoolId));
        }

        return results;
    }

    /**
     * Changes a user's password
     * @param email User's email
     * @param oldPassword Current password
     * @param newPassword New password to set
     * @return Updated UserAccount with new JWT token
     */
    @Transactional
    public UserAccount changePassword(String email, String oldPassword, String newPassword) {
        UserAccount user = userAccountRepository.findByEmailAndPassword(email, oldPassword)
            .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        SchoolContext.clear();
        user.setPassword(newPassword);
        user.setRequiresPasswordReset(false);
        user = userAccountRepository.save(user);

        // Generate new JWT token
        String token = jwtUtil.generateToken(user.getEmail(), user.getSchoolId());
        user.setToken(token);

        return user;
    }

    /**
     * DEVELOPMENT ONLY: Deletes all users and their associated tenant databases
     * WARNING: This is a destructive operation and should only be used in development
     */
    @Transactional
    public void deleteAllUsersAndTenants() {
        // Clear context to work with master DB
        SchoolContext.clear();

        // Get all schools first (so we can drop their databases)
        List<School> schools = schoolRepository.findAll();

        // Delete all user accounts
        userAccountRepository.deleteAll();

        // Delete all schools and their databases
        for (School school : schools) {
            tenantProvisioningService.dropTenantDatabase(school);
        }

        schoolRepository.deleteAll();
    }
}
