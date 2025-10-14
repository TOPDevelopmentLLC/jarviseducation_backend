package com.top.jarvised;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SchoolFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String schoolId = request.getHeader("X-School-ID");

        if (schoolId != null) {
            SchoolContext.setSchool(schoolId);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            SchoolContext.clear();
        }
    }
}

