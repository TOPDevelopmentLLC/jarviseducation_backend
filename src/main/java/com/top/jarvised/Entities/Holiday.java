package com.top.jarvised.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "holidays")
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate date;

    @Column
    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_year_settings_id", nullable = false)
    private SchoolYearSettings schoolYearSettings;

    public Holiday() {}

    public Holiday(String name, LocalDate date, String description,
                   SchoolYearSettings schoolYearSettings) {
        this.name = name;
        this.date = date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
