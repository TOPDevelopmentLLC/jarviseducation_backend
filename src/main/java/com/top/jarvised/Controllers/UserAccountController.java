package com.top.jarvised.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.top.jarvised.DTOs.BatchCreateUsersRequest;
import com.top.jarvised.DTOs.ChangePasswordRequest;
import com.top.jarvised.DTOs.UserCreationResult;
import com.top.jarvised.Entities.UserAccount;
import com.top.jarvised.JwtUtil;
import com.top.jarvised.Services.UserAccountService;

@RestController
@RequestMapping("/auth")
public class UserAccountController {

    private UserAccountService userAccountService;
    private JwtUtil jwtUtil;

    @Autowired
    public UserAccountController(UserAccountService userAccountService, JwtUtil jwtUtil) {
        this.userAccountService = userAccountService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/version")
    public ResponseEntity<?> version() {
        return ResponseEntity.ok(Map.of("version", "2.0-shouldNotFilter-added"));
    }

    @GetMapping("/test-sign-up")
    public ResponseEntity<?> testSignUp() {
        return ResponseEntity.ok(Map.of("message", "If you see this, GET requests work for sign-up path"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> attemptLogin(@RequestBody Map<String, String> loginRequest) {
        try {
            UserAccount user = userAccountService.attemptLogin(
                loginRequest.get("email"),
                loginRequest.get("password")
            );

            // Return user with requiresPasswordReset flag
            return ResponseEntity.ok(Map.of(
                "user", user,
                "requiresPasswordReset", user.getRequiresPasswordReset()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Login failed: " + e.getMessage()));
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> createAccount(@RequestBody Map<String, String> signupRequest) {
        try {
            String email = signupRequest.get("email");
            String password = signupRequest.get("password");
            String schoolName = signupRequest.get("schoolName");

            if (schoolName == null || schoolName.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "School name is required"));
            }

            UserAccount user = userAccountService.createAccount(email, password, schoolName);
            return ResponseEntity.ok(Map.of("user", user));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Sign up failed: " + e.getMessage()));
        }
    }

    /**
     * Admin endpoint to create users in their tenant (supports single or batch)
     * Requires JWT authentication
     */
    @PostMapping("/admin/create-users-batch")
    public ResponseEntity<?> createUsersBatch(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody BatchCreateUsersRequest request) {
        try {
            // Extract JWT and get schoolId
            String token = authHeader.replace("Bearer ", "");
            Long adminSchoolId = jwtUtil.extractSchoolId(token);

            if (adminSchoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            // Create all users
            List<UserCreationResult> results = userAccountService.createUsersForTenantBatch(
                request.getUsers(), adminSchoolId);

            return ResponseEntity.ok(Map.of("results", results));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to create users: " + e.getMessage()));
        }
    }

    /**
     * Endpoint for users to change their password (first-time login)
     * Requires JWT authentication
     */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ChangePasswordRequest request) {
        try {
            // Extract JWT and get user email
            String token = authHeader.replace("Bearer ", "");
            String email = jwtUtil.extractUsername(token);

            if (email == null || email.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing user email"));
            }

            UserAccount user = userAccountService.changePassword(
                email,
                request.getOldPassword(),
                request.getNewPassword()
            );

            return ResponseEntity.ok(Map.of("user", user));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to change password: " + e.getMessage()));
        }
    }

    /**
     * DEVELOPMENT ONLY: Cleanup endpoint to remove all users and tenant databases
     * WARNING: This should be removed or secured before production use
     */
    @PostMapping("/admin/cleanup-all")
    public ResponseEntity<?> cleanupAll() {
        try {
            userAccountService.deleteAllUsersAndTenants();
            return ResponseEntity.ok(Map.of("message", "All users and tenant databases deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to cleanup: " + e.getMessage()));
        }
    }

}
