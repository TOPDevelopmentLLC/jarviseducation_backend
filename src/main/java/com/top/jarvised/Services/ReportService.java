package com.top.jarvised.Services;

import com.top.jarvised.DTOs.AddCommentRequest;
import com.top.jarvised.DTOs.CreateReportRequest;
import com.top.jarvised.DTOs.EditCommentRequest;
import com.top.jarvised.Entities.Comment;
import com.top.jarvised.Entities.Report;
import com.top.jarvised.Entities.Student;
import com.top.jarvised.Enums.ReportType;
import com.top.jarvised.Repositories.CommentRepository;
import com.top.jarvised.Repositories.ReportRepository;
import com.top.jarvised.Repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final CommentRepository commentRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository, CommentRepository commentRepository, StudentRepository studentRepository) {
        this.reportRepository = reportRepository;
        this.commentRepository = commentRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Get all comments for a specific report
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Comment> getCommentsByReportId(Long reportId) {
        // Verify report exists
        reportRepository.findById(reportId)
            .orElseThrow(() -> new RuntimeException("Report not found"));

        return commentRepository.findByReport_IdOrderByTimestampDesc(reportId);
    }

    /**
     * Add a comment to a report
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Comment addComment(Long reportId, AddCommentRequest request) {
        // Validate request
        if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        if (request.getBodyText() == null || request.getBodyText().trim().isEmpty()) {
            throw new IllegalArgumentException("Comment body text is required");
        }

        // Verify report exists
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new RuntimeException("Report not found"));

        // Create and save comment
        Comment comment = new Comment(request.getFullName(), request.getBodyText(), report);
        return commentRepository.save(comment);
    }

    /**
     * Edit a comment on a report
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Comment editComment(Long reportId, Long commentId, EditCommentRequest request) {
        // Validate request
        if (request.getBodyText() == null || request.getBodyText().trim().isEmpty()) {
            throw new IllegalArgumentException("Comment body text is required");
        }

        // Verify report exists
        reportRepository.findById(reportId)
            .orElseThrow(() -> new RuntimeException("Report not found"));

        // Verify comment exists and belongs to this report
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getReport().getId().equals(reportId)) {
            throw new IllegalArgumentException("Comment does not belong to this report");
        }

        // Update comment
        comment.setBodyText(request.getBodyText());
        comment.setEdited(true);
        comment.setLastEditedTimestamp(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    /**
     * Get all reports
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    /**
     * Create a new report
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Report createReport(CreateReportRequest request) {
        // Validate request
        if (request.getReportType() == null) {
            throw new IllegalArgumentException("Report type is required");
        }
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }
        if (request.getReportedByName() == null || request.getReportedByName().trim().isEmpty()) {
            throw new IllegalArgumentException("Reported by name is required");
        }
        if (request.getReportedById() == null) {
            throw new IllegalArgumentException("Reported by ID is required");
        }

        // Validate moodType is provided when reportType is Mood
        if (request.getReportType() == ReportType.Mood && request.getMoodType() == null) {
            throw new IllegalArgumentException("Mood type is required for Mood reports");
        }

        // Validate studentId is provided
        if (request.getStudentId() == null) {
            throw new IllegalArgumentException("Student ID is required");
        }

        // Find the student
        Student student = studentRepository.findById(request.getStudentId())
            .orElseThrow(() -> new RuntimeException("Student not found"));

        // Create and save report
        Report report = new Report(
            request.getReportType(),
            request.getDescription(),
            request.getReportedByName(),
            request.getReportedById(),
            request.getMoodType(),
            student
        );

        return reportRepository.save(report);
    }
}
