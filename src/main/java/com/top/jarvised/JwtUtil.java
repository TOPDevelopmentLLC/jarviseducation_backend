package com.top.jarvised;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    private final SecretKey key;
    private static final String SCHOOL_ID_CLAIM = "schoolId";

    public JwtUtil(SecretKey key) {
        this.key = key;
    }

    /**
     * Generates a JWT token with username and schoolId
     * @param username User's email/username
     * @param schoolId The school ID for tenant routing
     * @return JWT token string
     */
    public String generateToken(String username, Long schoolId) {
        return Jwts.builder()
                .setSubject(username)
                .claim(SCHOOL_ID_CLAIM, schoolId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts username from token
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Extracts schoolId from token
     */
    public Long extractSchoolId(String token) {
        Object schoolIdObj = extractClaims(token).get(SCHOOL_ID_CLAIM);
        if (schoolIdObj instanceof Integer) {
            return ((Integer) schoolIdObj).longValue();
        } else if (schoolIdObj instanceof Long) {
            return (Long) schoolIdObj;
        }
        return null;
    }

    /**
     * Validates token against username
     */
    public boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    /**
     * Extracts all claims from token
     */
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if token is expired
     */
    private boolean isTokenExpired(String token) {
        return extractClaims(token)
                .getExpiration()
                .before(new Date());
    }
}
