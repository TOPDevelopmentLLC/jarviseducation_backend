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

import com.top.jarvised.JwtUtil;
import com.top.jarvised.SchoolContext;
import com.top.jarvised.DTOs.StudentResponse;
import com.top.jarvised.Entities.Student;
import com.top.jarvised.Entities.UserAccount;
import com.top.jarvised.Repositories.UserAccountRepository;
import com.top.jarvised.Services.StudentService;

@RestController
public class StudentController {

    private StudentService studentService;
    private JwtUtil jwtUtil;
    private UserAccountRepository userAccountRepository;

    @Autowired
    public StudentController(
            StudentService studentService,
            JwtUtil jwtUtil,
            UserAccountRepository userAccountRepository) {
        this.studentService = studentService;
        this.jwtUtil = jwtUtil;
        this.userAccountRepository = userAccountRepository;
    }

    @GetMapping("/students")
    public ResponseEntity<?> getAllStudents(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);
            String email = jwtUtil.extractUsername(token);

            System.out.println("[DEBUG] GET /students - schoolId from JWT: " + schoolId);
            System.out.println("[DEBUG] GET /students - email from JWT: " + email);

            // Get user account ID from master DB using service (handles its own transaction)
            Long userAccountId = studentService.getUserAccountIdByEmail(email);

            System.out.println("[DEBUG] GET /students - user found, id: " + userAccountId);

            // Set school context for tenant DB queries
            SchoolContext.setSchool(schoolId.toString());
            System.out.println("[DEBUG] GET /students - SchoolContext set to: " + SchoolContext.getSchool());

            List<StudentResponse> students = studentService.getAllStudents(schoolId, userAccountId);
            System.out.println("[DEBUG] GET /students - found " + students.size() + " students");

            return ResponseEntity.ok(students);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to get students: " + e.getMessage()));
        }
    }

    @PostMapping("/students")
    public ResponseEntity<?> createStudent(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Student student) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            System.out.println("[DEBUG] POST /students - schoolId from JWT: " + schoolId);
            System.out.println("[DEBUG] POST /students - student name: " + student.getName());

            // Set school context for tenant DB
            SchoolContext.setSchool(schoolId.toString());
            System.out.println("[DEBUG] POST /students - SchoolContext set to: " + SchoolContext.getSchool());

            Student created = studentService.createStudent(student);
            System.out.println("[DEBUG] POST /students - created student with id: " + created.getId());

            return ResponseEntity.ok(created);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create student: " + e.getMessage()));
        }
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<?> deleteStudent(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            // Set school context for tenant DB
            SchoolContext.setSchool(schoolId.toString());

            studentService.deleteStudent(id);
            return ResponseEntity.ok(Map.of("message", "Student deleted successfully"));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to delete student: " + e.getMessage()));
        }
    }

}
