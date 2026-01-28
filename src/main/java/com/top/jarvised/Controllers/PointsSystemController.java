package com.top.jarvised.Controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.top.jarvised.JwtUtil;
import com.top.jarvised.SchoolContext;
import com.top.jarvised.DTOs.PointsSystemResponse;
import com.top.jarvised.DTOs.UpdatePointsSystemRequest;
import com.top.jarvised.Entities.UserAccount;
import com.top.jarvised.Repositories.UserAccountRepository;
import com.top.jarvised.Services.PointsSystemService;

@RestController
@RequestMapping("/api/points-system")
public class PointsSystemController {

    private final PointsSystemService pointsSystemService;
    private final JwtUtil jwtUtil;
    private final UserAccountRepository userAccountRepository;

    public PointsSystemController(
            PointsSystemService pointsSystemService,
            JwtUtil jwtUtil,
            UserAccountRepository userAccountRepository) {
        this.pointsSystemService = pointsSystemService;
        this.jwtUtil = jwtUtil;
        this.userAccountRepository = userAccountRepository;
    }

    /**
     * Get the points system for the authenticated user
     */
    @GetMapping
    public ResponseEntity<?> getPointsSystem(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String email = jwtUtil.extractUsername(token);

            // Clear context to query master DB
            SchoolContext.clear();

            UserAccount user = userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            PointsSystemResponse response = pointsSystemService.getPointsSystem(user.getId());
            return ResponseEntity.ok(Map.of("pointsSystem", response));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to get points system: " + e.getMessage()));
        }
    }

    /**
     * Update the points system for the authenticated user
     */
    @PutMapping
    public ResponseEntity<?> updatePointsSystem(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UpdatePointsSystemRequest request) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String email = jwtUtil.extractUsername(token);

            // Clear context to query master DB
            SchoolContext.clear();

            UserAccount user = userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            PointsSystemResponse response = pointsSystemService.updatePointsSystem(user.getId(), request);
            return ResponseEntity.ok(Map.of("pointsSystem", response));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update points system: " + e.getMessage()));
        }
    }
}
