package com.top.jarvised.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.top.jarvised.JwtUtil;
import com.top.jarvised.Entities.UserAccount;
import com.top.jarvised.Repositories.UserAccountRepository;

@Service
public class UserAccountService {
    
    private UserAccountRepository userAccountRepository;
    private JwtUtil jwtUtil;

    @Autowired
    public UserAccountService(UserAccountRepository userAccountRepository, JwtUtil jwtUtil) {
        this.userAccountRepository = userAccountRepository;
        this.jwtUtil = jwtUtil;
    }

    public UserAccount attemptLogin(String email, String password) {
        UserAccount user = userAccountRepository.findByEmailAndPassword(email,password)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }
        user.setToken(jwtUtil.generateToken(user.getEmail()));
        return user;
    }

    public UserAccount createAccount(String email, String password) {
        if (userAccountRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        return userAccountRepository.save(new UserAccount(email, password));
    }
}
