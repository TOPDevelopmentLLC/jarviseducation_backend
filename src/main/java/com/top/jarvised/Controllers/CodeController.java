package com.top.jarvised.Controllers;

import com.top.jarvised.DTOs.CreateCodeRequest;
import com.top.jarvised.DTOs.UpdateCodeRequest;
import com.top.jarvised.Entities.Code;
import com.top.jarvised.JwtUtil;
import com.top.jarvised.Services.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/codes")
public class CodeController {

    private CodeService codeService;
    private JwtUtil jwtUtil;

    @Autowired
    public CodeController(CodeService codeService, JwtUtil jwtUtil) {
        this.codeService = codeService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Get all codes
     */
    @GetMapping
    public ResponseEntity<?> getAllCodes(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            List<Code> codes = codeService.getAllCodes();
            return ResponseEntity.ok(Map.of("codes", codes));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to fetch codes: " + e.getMessage()));
        }
    }

    /**
     * Get a specific code by ID
     */
    @GetMapping("/{codeId}")
    public ResponseEntity<?> getCodeById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long codeId) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            Code code = codeService.getCodeById(codeId);
            return ResponseEntity.ok(Map.of("code", code));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to fetch code: " + e.getMessage()));
        }
    }

    /**
     * Create a new code
     */
    @PostMapping
    public ResponseEntity<?> createCode(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CreateCodeRequest request) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            Code code = codeService.createCode(request);
            return ResponseEntity.ok(Map.of("code", code));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create code: " + e.getMessage()));
        }
    }

    /**
     * Update an existing code
     */
    @PutMapping("/{codeId}")
    public ResponseEntity<?> updateCode(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long codeId,
            @RequestBody UpdateCodeRequest request) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            Code code = codeService.updateCode(codeId, request);
            return ResponseEntity.ok(Map.of("code", code));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update code: " + e.getMessage()));
        }
    }

    /**
     * Delete a code
     */
    @DeleteMapping("/{codeId}")
    public ResponseEntity<?> deleteCode(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long codeId) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            codeService.deleteCode(codeId);
            return ResponseEntity.ok(Map.of("message", "Code deleted successfully"));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to delete code: " + e.getMessage()));
        }
    }
}
