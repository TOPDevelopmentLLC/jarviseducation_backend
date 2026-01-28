package com.top.jarvised.DTOs;

import com.top.jarvised.Entities.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class SchoolYearSettingsResponse {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String termType;
    private LocalTime schoolDayStart;
    private LocalTime schoolDayEnd;
    private String timezone;
    private boolean isActive;
    private Long schoolId;
    private List<TermInfo> terms;
    private List<HolidayInfo> holidays;
    private List<BreakPeriodInfo> breakPeriods;
    private List<SchedulePeriodInfo> schedulePeriods;

    public SchoolYearSettingsResponse() {}

    public SchoolYearSettingsResponse(SchoolYearSettings settings) {
        this.id = settings.getId();
        this.name = settings.getName();
        this.startDate = settings.getStartDate();
        this.endDate = settings.getEndDate();
        this.termType = settings.getTermType().toString();
        this.schoolDayStart = settings.getSchoolDayStart();
        this.schoolDayEnd = settings.getSchoolDayEnd();
        this.timezone = settings.getTimezone();
        this.isActive = settings.isActive();
        this.schoolId = settings.getSchoolId();
        this.terms = settings.getTerms().stream()
            .map(TermInfo::new)
            .collect(Collectors.toList());
        this.holidays = settings.getHolidays().stream()
            .map(HolidayInfo::new)
            .collect(Collectors.toList());
        this.breakPeriods = settings.getBreakPeriods().stream()
            .map(BreakPeriodInfo::new)
            .collect(Collectors.toList());
        this.schedulePeriods = settings.getSchedulePeriods().stream()
            .map(SchedulePeriodInfo::new)
            .collect(Collectors.toList());
    }

    // Nested response classes
    public static class TermInfo {
        private Long id;
        private String name;
        private Integer termNumber;
        private LocalDate startDate;
        private LocalDate endDate;

        public TermInfo() {}

        public TermInfo(Term term) {
            this.id = term.getId();
            this.name = term.getName();
            this.termNumber = term.getTermNumber();
            this.startDate = term.getStartDate();
            this.endDate = term.getEndDate();
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Integer getTermNumber() {
            return termNumber;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }
    }

    public static class HolidayInfo {
        private Long id;
        private String name;
        private LocalDate date;
        private String description;

        public HolidayInfo() {}

        public HolidayInfo(Holiday holiday) {
            this.id = holiday.getId();
            this.name = holiday.getName();
            this.date = holiday.getDate();
            this.description = holiday.getDescription();
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public LocalDate getDate() {
            return date;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class BreakPeriodInfo {
        private Long id;
        private String name;
        private String breakType;
        private LocalDate startDate;
        private LocalDate endDate;
        private String description;

        public BreakPeriodInfo() {}

        public BreakPeriodInfo(BreakPeriod breakPeriod) {
            this.id = breakPeriod.getId();
            this.name = breakPeriod.getName();
            this.breakType = breakPeriod.getBreakType().toString();
            this.startDate = breakPeriod.getStartDate();
            this.endDate = breakPeriod.getEndDate();
            this.description = breakPeriod.getDescription();
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getBreakType() {
            return breakType;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class SchedulePeriodInfo {
        private Long id;
        private String name;
        private Integer periodNumber;
        private String periodType;
        private LocalTime startTime;
        private LocalTime endTime;

        public SchedulePeriodInfo() {}

        public SchedulePeriodInfo(SchedulePeriod period) {
            this.id = period.getId();
            this.name = period.getName();
            this.periodNumber = period.getPeriodNumber();
            this.periodType = period.getPeriodType().toString();
            this.startTime = period.getStartTime();
            this.endTime = period.getEndTime();
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Integer getPeriodNumber() {
            return periodNumber;
        }

        public String getPeriodType() {
            return periodType;
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }
    }

    // Main class getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getTermType() {
        return termType;
    }

    public LocalTime getSchoolDayStart() {
        return schoolDayStart;
    }

    public LocalTime getSchoolDayEnd() {
        return schoolDayEnd;
    }

    public String getTimezone() {
        return timezone;
    }

    public boolean isActive() {
        return isActive;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public List<TermInfo> getTerms() {
        return terms;
    }

    public List<HolidayInfo> getHolidays() {
        return holidays;
    }

    public List<BreakPeriodInfo> getBreakPeriods() {
        return breakPeriods;
    }

    public List<SchedulePeriodInfo> getSchedulePeriods() {
        return schedulePeriods;
    }
}
