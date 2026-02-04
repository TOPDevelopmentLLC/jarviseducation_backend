package com.top.jarvised.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.top.jarvised.Enums.MoodType;
import com.top.jarvised.Enums.ReportType;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private ReportType reportType;
    private String description;
    @Nullable
    private MoodType moodType;
    private String reportedByName;
    private Long reportedById;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @JsonIgnore
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Report() {}

    public Report(ReportType reportType, String description, String reportedByName, Long reportedById, @Nullable MoodType moodType, Student student) {
        this.reportType = reportType;
        this.description = description;
        this.reportedByName = reportedByName;
        this.reportedById = reportedById;
        this.student = student;
        if (reportType == ReportType.Mood) {
            this.moodType = moodType;
        } else {
            this.moodType = null;
        }
    }

    public Long getId() {
        return this.id;
    }
    public ReportType getReportType() {
        return this.reportType;
    }
    public String getDescription() {
        return this.description;
    }
    public MoodType getMoodType() {
        return this.moodType;
    }
    public String getReportedByName() {
        return this.reportedByName;
    }
    public Long getReportedById() {
        return this.reportedById;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setReport(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setReport(null);
    }
}
