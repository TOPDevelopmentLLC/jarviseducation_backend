package com.top.jarvised.Services;

import com.top.jarvised.DTOs.AddCommentRequest;
import com.top.jarvised.DTOs.EditCommentRequest;
import com.top.jarvised.Entities.Comment;
import com.top.jarvised.Entities.Report;
import com.top.jarvised.Repositories.CommentRepository;
import com.top.jarvised.Repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository, CommentRepository commentRepository) {
        this.reportRepository = reportRepository;
        this.commentRepository = commentRepository;
    }

    /**
     * Get all comments for a specific report
     */
    public List<Comment> getCommentsByReportId(Long reportId) {
        // Verify report exists
        reportRepository.findById(reportId)
            .orElseThrow(() -> new RuntimeException("Report not found"));

        return commentRepository.findByReport_IdOrderByTimestampDesc(reportId);
    }

    /**
     * Add a comment to a report
     */
    @Transactional
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
    @Transactional
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
}
