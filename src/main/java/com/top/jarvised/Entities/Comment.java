package com.top.jarvised.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String bodyText;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private boolean edited = false;

    @Column
    private LocalDateTime lastEditedTimestamp;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    public Comment() {
        this.timestamp = LocalDateTime.now();
        this.edited = false;
    }

    public Comment(String fullName, String bodyText, Report report) {
        this.fullName = fullName;
        this.bodyText = bodyText;
        this.report = report;
        this.timestamp = LocalDateTime.now();
        this.edited = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public LocalDateTime getLastEditedTimestamp() {
        return lastEditedTimestamp;
    }

    public void setLastEditedTimestamp(LocalDateTime lastEditedTimestamp) {
        this.lastEditedTimestamp = lastEditedTimestamp;
    }
}
