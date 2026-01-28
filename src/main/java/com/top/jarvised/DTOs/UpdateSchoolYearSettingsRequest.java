package com.top.jarvised.DTOs;

import java.time.LocalDate;
import java.time.LocalTime;

public class UpdateSchoolYearSettingsRequest {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String termType;
    private LocalTime schoolDayStart;
    private LocalTime schoolDayEnd;
    private String timezone;

    public UpdateSchoolYearSettingsRequest() {}

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

    public String getTermType() {
        return termType;
    }

    public void setTermType(String termType) {
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
}
