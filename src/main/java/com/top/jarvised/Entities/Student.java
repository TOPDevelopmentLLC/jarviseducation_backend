package com.top.jarvised.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    // private Optional<Long> userAccountID;
    // private Optional<Long> parentAccountID;

    private boolean isActive = true;

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    public Student() {}

    public Student(String name) {
        this.name = name;
        this.isActive = true;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<Report> getReports() {
        return this.reports;
    }

    public void addReport(Report report) {
        reports.add(report);
        report.setStudent(this);
    }

    public void removeReport(Report report) {
        reports.remove(report);
        report.setStudent(null);
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}