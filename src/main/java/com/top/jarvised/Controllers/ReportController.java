package com.top.jarvised.Controllers;

import com.top.jarvised.DTOs.AddCommentRequest;
import com.top.jarvised.DTOs.EditCommentRequest;
import com.top.jarvised.Entities.Comment;
import com.top.jarvised.JwtUtil;
import com.top.jarvised.Services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;
    private final JwtUtil jwtUtil;

    @Autowired
    public ReportController(ReportService reportService, JwtUtil jwtUtil) {
        this.reportService = reportService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Get all comments for a specific report
     */
    @GetMapping("/{reportId}/comments")
    public ResponseEntity<?> getComments(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long reportId) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            List<Comment> comments = reportService.getCommentsByReportId(reportId);
            return ResponseEntity.ok(Map.of("comments", comments));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to fetch comments: " + e.getMessage()));
        }
    }

    /**
     * Add a comment to a report
     */
    @PostMapping("/{reportId}/comments")
    public ResponseEntity<?> addComment(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long reportId,
            @RequestBody AddCommentRequest request) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            Comment comment = reportService.addComment(reportId, request);
            return ResponseEntity.ok(Map.of("comment", comment));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to add comment: " + e.getMessage()));
        }
    }

    /**
     * Edit a comment on a report
     */
    @PutMapping("/{reportId}/comments/{commentId}")
    public ResponseEntity<?> editComment(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long reportId,
            @PathVariable Long commentId,
            @RequestBody EditCommentRequest request) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            Comment comment = reportService.editComment(reportId, commentId, request);
            return ResponseEntity.ok(Map.of("comment", comment));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to edit comment: " + e.getMessage()));
        }
    }
}
