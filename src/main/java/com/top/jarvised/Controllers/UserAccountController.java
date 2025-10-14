package com.top.jarvised.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.top.jarvised.Entities.UserAccount;
import com.top.jarvised.Services.UserAccountService;

@RestController
@RequestMapping("/auth")
public class UserAccountController {

    private UserAccountService userAccountService;

    @Autowired
    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, UserAccount>> attemptLogin(@RequestBody Map<String, String> loginRequest) {
        return ResponseEntity.ok(Map.of("user", userAccountService.attemptLogin(loginRequest.get("email"), loginRequest.get("password"))));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, UserAccount>> createAccount(@RequestBody Map<String, String> signupRequest) {
        String email = signupRequest.get("email");
        String password = signupRequest.get("password");
        String schoolName = signupRequest.get("schoolName");

        if (schoolName == null || schoolName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", new UserAccount()));
        }

        UserAccount user = userAccountService.createAccount(email, password, schoolName);
        return ResponseEntity.ok(Map.of("user", user));
    }
    
}
