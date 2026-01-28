package com.top.jarvised.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "terms")
public class Term {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer termNumber;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_year_settings_id", nullable = false)
    private SchoolYearSettings schoolYearSettings;

    public Term() {}

    public Term(String name, Integer termNumber, LocalDate startDate,
                LocalDate endDate, SchoolYearSettings schoolYearSettings) {
        this.name = name;
        this.termNumber = termNumber;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public Integer getTermNumber() {
        return termNumber;
    }

    public void setTermNumber(Integer termNumber) {
        this.termNumber = termNumber;
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

    public SchoolYearSettings getSchoolYearSettings() {
        return schoolYearSettings;
    }

    public void setSchoolYearSettings(SchoolYearSettings schoolYearSettings) {
        this.schoolYearSettings = schoolYearSettings;
    }
}
