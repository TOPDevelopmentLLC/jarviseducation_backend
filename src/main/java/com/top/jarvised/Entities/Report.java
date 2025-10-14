package com.top.jarvised.Entities;

import com.top.jarvised.Enums.MoodType;
import com.top.jarvised.Enums.ReportType;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

    public Report(ReportType reportType, String description, String reportedByName, Long reportedById, @Nullable MoodType moodType) {
        this.reportType = reportType;
        this.description = description;
        this.reportedByName = reportedByName;
        this.reportedById = reportedById;
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
}
