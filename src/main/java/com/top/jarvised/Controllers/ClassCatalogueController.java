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

import com.top.jarvised.Entities.ClassCatalogueItem;
import com.top.jarvised.JwtUtil;
import com.top.jarvised.SchoolContext;
import com.top.jarvised.Services.ClassCatalogueService;

@RestController
public class ClassCatalogueController {

    private ClassCatalogueService classCatalogueService;
    private JwtUtil jwtUtil;

    @Autowired
    public ClassCatalogueController(ClassCatalogueService classCatalogueService, JwtUtil jwtUtil) {
        this.classCatalogueService = classCatalogueService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/classcatalogue")
    public ResponseEntity<?> getAllClasses(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            SchoolContext.setSchool(schoolId.toString());

            List<ClassCatalogueItem> classes = classCatalogueService.getAllClasses();
            return ResponseEntity.ok(classes);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to get classes: " + e.getMessage()));
        }
    }

    @PostMapping("/classcatalogue")
    public ResponseEntity<?> createClass(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ClassCatalogueItem classCatalogue) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            SchoolContext.setSchool(schoolId.toString());

            ClassCatalogueItem created = classCatalogueService.createClass(classCatalogue);
            return ResponseEntity.ok(created);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create class: " + e.getMessage()));
        }
    }

    @DeleteMapping("/classcatalogue/{id}")
    public ResponseEntity<?> deleteClass(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            SchoolContext.setSchool(schoolId.toString());

            classCatalogueService.deleteClass(id);
            return ResponseEntity.ok(Map.of("message", "Course deleted successfully"));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to delete class: " + e.getMessage()));
        }
    }

}
