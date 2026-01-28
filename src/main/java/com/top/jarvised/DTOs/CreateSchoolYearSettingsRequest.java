package com.top.jarvised.DTOs;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CreateSchoolYearSettingsRequest {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String termType;
    private LocalTime schoolDayStart;
    private LocalTime schoolDayEnd;
    private String timezone;
    private boolean isActive;
    private List<TermDTO> terms;
    private List<HolidayDTO> holidays;
    private List<BreakPeriodDTO> breakPeriods;
    private List<SchedulePeriodDTO> schedulePeriods;

    public CreateSchoolYearSettingsRequest() {}

    // Nested DTOs for child entities
    public static class TermDTO {
        private String name;
        private Integer termNumber;
        private LocalDate startDate;
        private LocalDate endDate;

        public TermDTO() {}

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
    }

    public static class HolidayDTO {
        private String name;
        private LocalDate date;
        private String description;

        public HolidayDTO() {}

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
    }

    public static class BreakPeriodDTO {
        private String name;
        private String breakType;
        private LocalDate startDate;
        private LocalDate endDate;
        private String description;

        public BreakPeriodDTO() {}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBreakType() {
            return breakType;
        }

        public void setBreakType(String breakType) {
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
    }

    public static class SchedulePeriodDTO {
        private String name;
        private Integer periodNumber;
        private String periodType;
        private LocalTime startTime;
        private LocalTime endTime;

        public SchedulePeriodDTO() {}

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

        public String getPeriodType() {
            return periodType;
        }

        public void setPeriodType(String periodType) {
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
    }

    // Main class getters and setters
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<TermDTO> getTerms() {
        return terms;
    }

    public void setTerms(List<TermDTO> terms) {
        this.terms = terms;
    }

    public List<HolidayDTO> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<HolidayDTO> holidays) {
        this.holidays = holidays;
    }

    public List<BreakPeriodDTO> getBreakPeriods() {
        return breakPeriods;
    }

    public void setBreakPeriods(List<BreakPeriodDTO> breakPeriods) {
        this.breakPeriods = breakPeriods;
    }

    public List<SchedulePeriodDTO> getSchedulePeriods() {
        return schedulePeriods;
    }

    public void setSchedulePeriods(List<SchedulePeriodDTO> schedulePeriods) {
        this.schedulePeriods = schedulePeriods;
    }
}
