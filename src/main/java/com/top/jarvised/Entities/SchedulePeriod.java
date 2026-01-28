package com.top.jarvised.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.top.jarvised.Enums.PeriodType;
import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "schedule_periods")
public class SchedulePeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer periodNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PeriodType periodType;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_year_settings_id", nullable = false)
    private SchoolYearSettings schoolYearSettings;

    public SchedulePeriod() {}

    public SchedulePeriod(String name, Integer periodNumber, PeriodType periodType,
                          LocalTime startTime, LocalTime endTime,
                          SchoolYearSettings schoolYearSettings) {
        this.name = name;
        this.periodNumber = periodNumber;
        this.periodType = periodType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.schoolYearSettings = schoolYearSettings;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPeriodNumber() {
        return periodNumber;
    }

    public void setPeriodNumber(Integer periodNumber) {
        this.periodNumber = periodNumber;
    }

    public PeriodType getPeriodType() {
        return periodType;
    }

    public void setPeriodType(PeriodType periodType) {
        this.periodType = periodType;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public SchoolYearSettings getSchoolYearSettings() {
        return schoolYearSettings;
    }

    public void setSchoolYearSettings(SchoolYearSettings schoolYearSettings) {
        this.schoolYearSettings = schoolYearSettings;
    }
}
