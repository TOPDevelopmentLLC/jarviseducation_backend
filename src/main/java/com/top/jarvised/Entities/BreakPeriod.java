package com.top.jarvised.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.top.jarvised.Enums.BreakType;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "break_periods")
public class BreakPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BreakType breakType;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column
    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_year_settings_id", nullable = false)
    private SchoolYearSettings schoolYearSettings;

    public BreakPeriod() {}

    public BreakPeriod(String name, BreakType breakType, LocalDate startDate,
                       LocalDate endDate, String description,
                       SchoolYearSettings schoolYearSettings) {
        this.name = name;
        this.breakType = breakType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
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

    public BreakType getBreakType() {
        return breakType;
    }

    public void setBreakType(BreakType breakType) {
        this.breakType = breakType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SchoolYearSettings getSchoolYearSettings() {
        return schoolYearSettings;
    }

    public void setSchoolYearSettings(SchoolYearSettings schoolYearSettings) {
        this.schoolYearSettings = schoolYearSettings;
    }
}
