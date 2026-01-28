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

import com.top.jarvised.Entities.Parent;
import com.top.jarvised.JwtUtil;
import com.top.jarvised.SchoolContext;
import com.top.jarvised.Services.ParentService;

@RestController
public class ParentController {

    private ParentService parentService;
    private JwtUtil jwtUtil;

    @Autowired
    public ParentController(ParentService parentService, JwtUtil jwtUtil) {
        this.parentService = parentService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/parents")
    public ResponseEntity<?> getAllParents(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            SchoolContext.setSchool(schoolId.toString());

            List<Parent> parents = parentService.getAllStudents();
            return ResponseEntity.ok(parents);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to get parents: " + e.getMessage()));
        }
    }

    @PostMapping("/parents")
    public ResponseEntity<?> createParent(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Parent parent) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            SchoolContext.setSchool(schoolId.toString());

            Parent created = parentService.createParent(parent);
            return ResponseEntity.ok(created);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create parent: " + e.getMessage()));
        }
    }

    @DeleteMapping("/parents/{id}")
    public ResponseEntity<?> deleteParent(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            SchoolContext.setSchool(schoolId.toString());

            parentService.deleteParent(id);
            return ResponseEntity.ok(Map.of("message", "Parent deleted successfully"));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to delete parent: " + e.getMessage()));
        }
    }

}
