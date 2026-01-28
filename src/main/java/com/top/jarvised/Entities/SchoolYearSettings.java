package com.top.jarvised.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.top.jarvised.Enums.TermType;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "school_year_settings")
public class SchoolYearSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TermType termType;

    @Column
    private LocalTime schoolDayStart;

    @Column
    private LocalTime schoolDayEnd;

    @Column(nullable = false)
    private String timezone;

    @Column(nullable = false)
    private boolean isActive = false;

    @Column(nullable = false)
    private Long schoolId;

    @JsonIgnore
    @OneToMany(mappedBy = "schoolYearSettings", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Term> terms = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "schoolYearSettings", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Holiday> holidays = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "schoolYearSettings", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BreakPeriod> breakPeriods = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "schoolYearSettings", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SchedulePeriod> schedulePeriods = new ArrayList<>();

    public SchoolYearSettings() {}

    public SchoolYearSettings(String name, LocalDate startDate, LocalDate endDate,
                              TermType termType, LocalTime schoolDayStart,
                              LocalTime schoolDayEnd, String timezone, Long schoolId) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.termType = termType;
        this.schoolDayStart = schoolDayStart;
        this.schoolDayEnd = schoolDayEnd;
        this.timezone = timezone;
        this.schoolId = schoolId;
    }

    // Helper methods for managing child entities
    public void addTerm(Term term) {
        terms.add(term);
        term.setSchoolYearSettings(this);
    }

    public void removeTerm(Term term) {
        terms.remove(term);
        term.setSchoolYearSettings(null);
    }

    public void addHoliday(Holiday holiday) {
        holidays.add(holiday);
        holiday.setSchoolYearSettings(this);
    }

    public void removeHoliday(Holiday holiday) {
        holidays.remove(holiday);
        holiday.setSchoolYearSettings(null);
    }

    public void addBreakPeriod(BreakPeriod breakPeriod) {
        breakPeriods.add(breakPeriod);
        breakPeriod.setSchoolYearSettings(this);
    }

    public void removeBreakPeriod(BreakPeriod breakPeriod) {
        breakPeriods.remove(breakPeriod);
        breakPeriod.setSchoolYearSettings(null);
    }

    public void addSchedulePeriod(SchedulePeriod schedulePeriod) {
        schedulePeriods.add(schedulePeriod);
        schedulePeriod.setSchoolYearSettings(this);
    }

    public void removeSchedulePeriod(SchedulePeriod schedulePeriod) {
        schedulePeriods.remove(schedulePeriod);
        schedulePeriod.setSchoolYearSettings(null);
    }

    // Getters and setters
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

    public TermType getTermType() {
        return termType;
    }

    public void setTermType(TermType termType) {
        this.termType = termType;
    }

    public LocalTime getSchoolDayStart() {
        return schoolDayStart;
    }

    public void setSchoolDayStart(LocalTime schoolDayStart) {
        this.schoolDayStart = schoolDayStart;
    }

    public LocalTime getSchoolDayEnd() {
        return schoolDayEnd;
    }

    public void setSchoolDayEnd(LocalTime schoolDayEnd) {
        this.schoolDayEnd = schoolDayEnd;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    public List<Holiday> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<Holiday> holidays) {
        this.holidays = holidays;
    }

    public List<BreakPeriod> getBreakPeriods() {
        return breakPeriods;
    }

    public void setBreakPeriods(List<BreakPeriod> breakPeriods) {
        this.breakPeriods = breakPeriods;
    }

    public List<SchedulePeriod> getSchedulePeriods() {
        return schedulePeriods;
    }

    public void setSchedulePeriods(List<SchedulePeriod> schedulePeriods) {
        this.schedulePeriods = schedulePeriods;
    }
}
