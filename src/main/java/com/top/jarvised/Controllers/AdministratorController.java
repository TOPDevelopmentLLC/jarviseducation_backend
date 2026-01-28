package com.top.jarvised.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.top.jarvised.Entities.Administrator;
import com.top.jarvised.JwtUtil;
import com.top.jarvised.SchoolContext;
import com.top.jarvised.Services.AdministratorService;

@RestController
public class AdministratorController {

    private final AdministratorService administratorService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AdministratorController(AdministratorService administratorService, JwtUtil jwtUtil) {
        this.administratorService = administratorService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/administrator")
    public ResponseEntity<?> createAdministrator(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> postRequest) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            SchoolContext.setSchool(schoolId.toString());

            if (!postRequest.containsKey("name")) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Request is missing the name property"));
            }
            Administrator admin = administratorService.createAdministrator(postRequest.get("name"));
            return ResponseEntity.ok(admin);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create administrator: " + e.getMessage()));
        }
    }

    @GetMapping("/administrators")
    public ResponseEntity<?> getAllAdministrators(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            SchoolContext.setSchool(schoolId.toString());

            List<Administrator> admins = administratorService.getAllAdministrators();
            return ResponseEntity.ok(admins);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to get administrators: " + e.getMessage()));
        }
    }

    @DeleteMapping("/administrator/{id}")
    public ResponseEntity<?> deleteAdministrator(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            SchoolContext.setSchool(schoolId.toString());

            administratorService.deleteAdministrator(id);
            return ResponseEntity.ok(Map.of("message", "Administrator deleted successfully"));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to delete administrator: " + e.getMessage()));
        }
    }
}
