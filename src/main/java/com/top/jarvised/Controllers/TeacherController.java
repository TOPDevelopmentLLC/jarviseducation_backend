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

import com.top.jarvised.Entities.Teacher;
import com.top.jarvised.JwtUtil;
import com.top.jarvised.SchoolContext;
import com.top.jarvised.Services.TeacherService;

@RestController
public class TeacherController {

    private TeacherService teacherService;
    private JwtUtil jwtUtil;

    @Autowired
    public TeacherController(TeacherService teacherService, JwtUtil jwtUtil) {
        this.teacherService = teacherService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/teachers")
    public ResponseEntity<?> getAllTeachers(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            SchoolContext.setSchool(schoolId.toString());

            List<Teacher> teachers = teacherService.getAllTeachers();
            return ResponseEntity.ok(teachers);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to get teachers: " + e.getMessage()));
        }
    }

    @PostMapping("/teachers")
    public ResponseEntity<?> createTeacher(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Teacher teacher) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            SchoolContext.setSchool(schoolId.toString());

            Teacher created = teacherService.createTeacher(teacher);
            return ResponseEntity.ok(created);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create teacher: " + e.getMessage()));
        }
    }

    @DeleteMapping("/teachers/{id}")
    public ResponseEntity<?> deleteTeacher(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            SchoolContext.setSchool(schoolId.toString());

            teacherService.deleteTeacher(id);
            return ResponseEntity.ok(Map.of("message", "Teacher deleted successfully"));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to delete teacher: " + e.getMessage()));
        }
    }

}
