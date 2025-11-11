package com.top.jarvised;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.top.jarvised.Entities.UserAccount;
import com.top.jarvised.Repositories.UserAccountRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;
    private UserAccountRepository userAccountRepository;

    public JwtRequestFilter(UserAccountRepository userAccountRepository, JwtUtil jwtUtil) {
        this.userAccountRepository = userAccountRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // Skip filter for public authentication endpoints
        return path.equals("/auth/login") || path.equals("/auth/sign-up");
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;
        Long schoolId = null;

        // Extract token if it starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
                schoolId = jwtUtil.extractSchoolId(jwt);
            } catch (Exception e) {
                logger.error("JWT token is invalid: " + e.getMessage());
            }
        }

        // Set school context from JWT for tenant routing
        if (schoolId != null) {
            SchoolContext.setSchool(schoolId.toString());
        }

        try {
            // Only validate token and set authentication if JWT was present
            if (username != null && jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserAccount user = userAccountRepository.findByEmail(username).orElse(null);

                if (user != null && jwtUtil.validateToken(jwt, user.getEmail())) {
                    UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, List.of());

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } finally {
            // Always clear school context after request
            SchoolContext.clear();
        }
    }
}

