package com.top.jarvised.DTOs;

import com.top.jarvised.Enums.MoodType;
import com.top.jarvised.Enums.ReportType;

public class CreateReportRequest {
    private ReportType reportType;
    private String description;
    private MoodType moodType;
    private String reportedByName;
    private Long reportedById;
    private Long studentId;

    public CreateReportRequest() {}

    public CreateReportRequest(ReportType reportType, String description, String reportedByName,
                              Long reportedById, MoodType moodType, Long studentId) {
        this.reportType = reportType;
        this.description = description;
        this.reportedByName = reportedByName;
        this.reportedById = reportedById;
        this.moodType = moodType;
        this.studentId = studentId;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MoodType getMoodType() {
        return moodType;
    }

    public void setMoodType(MoodType moodType) {
        this.moodType = moodType;
    }

    public String getReportedByName() {
        return reportedByName;
    }

    public void setReportedByName(String reportedByName) {
        this.reportedByName = reportedByName;
    }

    public Long getReportedById() {
        return reportedById;
    }

    public void setReportedById(Long reportedById) {
        this.reportedById = reportedById;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
