package com.top.jarvised.Services;

import com.top.jarvised.DTOs.*;
import com.top.jarvised.Entities.*;
import com.top.jarvised.Enums.*;
import com.top.jarvised.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SchoolYearSettingsService {

    private final SchoolYearSettingsRepository settingsRepository;
    private final TermRepository termRepository;
    private final HolidayRepository holidayRepository;
    private final BreakPeriodRepository breakPeriodRepository;
    private final SchedulePeriodRepository schedulePeriodRepository;

    @Autowired
    public SchoolYearSettingsService(
            SchoolYearSettingsRepository settingsRepository,
            TermRepository termRepository,
            HolidayRepository holidayRepository,
            BreakPeriodRepository breakPeriodRepository,
            SchedulePeriodRepository schedulePeriodRepository) {
        this.settingsRepository = settingsRepository;
        this.termRepository = termRepository;
        this.holidayRepository = holidayRepository;
        this.breakPeriodRepository = breakPeriodRepository;
        this.schedulePeriodRepository = schedulePeriodRepository;
    }

    // ==================== School Year Settings CRUD ====================

    /**
     * Get all historical (inactive) school year settings
     */
    public List<SchoolYearSettingsResponse> getHistoricalSettings(Long schoolId) {
        return settingsRepository.findBySchoolId(schoolId).stream()
            .filter(settings -> !settings.isActive())
            .map(SchoolYearSettingsResponse::new)
            .collect(Collectors.toList());
    }

    public SchoolYearSettingsResponse getSettingsById(Long id, Long schoolId) {
        SchoolYearSettings settings = settingsRepository.findByIdAndSchoolId(id, schoolId)
            .orElseThrow(() -> new RuntimeException("School year settings not found"));
        return new SchoolYearSettingsResponse(settings);
    }

    public SchoolYearSettingsResponse getActiveSettings(Long schoolId) {
        SchoolYearSettings settings = settingsRepository.findBySchoolIdAndIsActiveTrue(schoolId)
            .orElseThrow(() -> new RuntimeException("No active school year settings found"));
        return new SchoolYearSettingsResponse(settings);
    }

    /**
     * Validates that the settings are active and can be modified
     */
    private void validateActiveSettings(SchoolYearSettings settings) {
        if (!settings.isActive()) {
            throw new IllegalStateException("Historical school year settings cannot be modified");
        }
    }

    @Transactional
    public SchoolYearSettingsResponse createSettings(CreateSchoolYearSettingsRequest request,
                                                      Long schoolId) {
        // Validate dates
        validateSchoolYearDates(request.getStartDate(), request.getEndDate());

        // Create the main settings entity
        SchoolYearSettings settings = new SchoolYearSettings(
            request.getName(),
            request.getStartDate(),
            request.getEndDate(),
            TermType.getFromString(request.getTermType()),
            request.getSchoolDayStart(),
            request.getSchoolDayEnd(),
            request.getTimezone(),
            schoolId
        );

        // If setting as active, deactivate any currently active settings
        if (request.isActive()) {
            deactivateCurrentSettings(schoolId);
            settings.setActive(true);
        }

        // Add terms
        if (request.getTerms() != null) {
            for (CreateSchoolYearSettingsRequest.TermDTO termDto : request.getTerms()) {
                validateTermDates(termDto.getStartDate(), termDto.getEndDate(),
                                  request.getStartDate(), request.getEndDate());
                Term term = new Term(termDto.getName(), termDto.getTermNumber(),
                                     termDto.getStartDate(), termDto.getEndDate(), settings);
                settings.addTerm(term);
            }
        }

        // Add holidays
        if (request.getHolidays() != null) {
            for (CreateSchoolYearSettingsRequest.HolidayDTO holidayDto : request.getHolidays()) {
                validateHolidayDate(holidayDto.getDate(), request.getStartDate(), request.getEndDate());
                Holiday holiday = new Holiday(holidayDto.getName(), holidayDto.getDate(),
                                              holidayDto.getDescription(), settings);
                settings.addHoliday(holiday);
            }
        }

        // Add break periods
        if (request.getBreakPeriods() != null) {
            for (CreateSchoolYearSettingsRequest.BreakPeriodDTO breakDto : request.getBreakPeriods()) {
                validateBreakDates(breakDto.getStartDate(), breakDto.getEndDate(),
                                   request.getStartDate(), request.getEndDate());
                BreakPeriod breakPeriod = new BreakPeriod(breakDto.getName(),
                    BreakType.getFromString(breakDto.getBreakType()),
                    breakDto.getStartDate(), breakDto.getEndDate(),
                    breakDto.getDescription(), settings);
                settings.addBreakPeriod(breakPeriod);
            }
        }

        // Add schedule periods
        if (request.getSchedulePeriods() != null) {
            for (CreateSchoolYearSettingsRequest.SchedulePeriodDTO periodDto : request.getSchedulePeriods()) {
                validateSchedulePeriodTimes(periodDto.getStartTime(), periodDto.getEndTime(),
                                            request.getSchoolDayStart(), request.getSchoolDayEnd());
                SchedulePeriod period = new SchedulePeriod(periodDto.getName(),
                    periodDto.getPeriodNumber(),
                    PeriodType.getFromString(periodDto.getPeriodType()),
                    periodDto.getStartTime(), periodDto.getEndTime(), settings);
                settings.addSchedulePeriod(period);
            }
        }

        settings = settingsRepository.save(settings);
        return new SchoolYearSettingsResponse(settings);
    }

    @Transactional
    public SchoolYearSettingsResponse updateActiveSettings(UpdateSchoolYearSettingsRequest request,
                                                            Long schoolId) {
        SchoolYearSettings settings = settingsRepository.findBySchoolIdAndIsActiveTrue(schoolId)
            .orElseThrow(() -> new RuntimeException("No active school year settings found"));

        if (request.getName() != null) {
            settings.setName(request.getName());
        }
        if (request.getStartDate() != null && request.getEndDate() != null) {
            validateSchoolYearDates(request.getStartDate(), request.getEndDate());
            settings.setStartDate(request.getStartDate());
            settings.setEndDate(request.getEndDate());
        }
        if (request.getTermType() != null) {
            settings.setTermType(TermType.getFromString(request.getTermType()));
        }
        if (request.getSchoolDayStart() != null) {
            settings.setSchoolDayStart(request.getSchoolDayStart());
        }
        if (request.getSchoolDayEnd() != null) {
            settings.setSchoolDayEnd(request.getSchoolDayEnd());
        }
        if (request.getTimezone() != null) {
            settings.setTimezone(request.getTimezone());
        }

        settings = settingsRepository.save(settings);
        return new SchoolYearSettingsResponse(settings);
    }

    @Transactional
    public void deleteSettings(Long id, Long schoolId) {
        SchoolYearSettings settings = settingsRepository.findByIdAndSchoolId(id, schoolId)
            .orElseThrow(() -> new RuntimeException("School year settings not found"));
        settingsRepository.delete(settings);
    }

    // ==================== Term Management ====================

    @Transactional
    public SchoolYearSettingsResponse addTerm(AddTermRequest request, Long schoolId) {
        SchoolYearSettings settings = settingsRepository.findBySchoolIdAndIsActiveTrue(schoolId)
            .orElseThrow(() -> new RuntimeException("No active school year settings found"));

        validateTermDates(request.getStartDate(), request.getEndDate(),
                         settings.getStartDate(), settings.getEndDate());

        Term term = new Term(request.getName(), request.getTermNumber(),
                            request.getStartDate(), request.getEndDate(), settings);
        settings.addTerm(term);

        settings = settingsRepository.save(settings);
        return new SchoolYearSettingsResponse(settings);
    }

    @Transactional
    public void removeTerm(Long termId, Long schoolId) {
        SchoolYearSettings settings = settingsRepository.findBySchoolIdAndIsActiveTrue(schoolId)
            .orElseThrow(() -> new RuntimeException("No active school year settings found"));

        Term term = termRepository.findByIdAndSchoolYearSettingsId(termId, settings.getId())
            .orElseThrow(() -> new RuntimeException("Term not found"));

        settings.removeTerm(term);
        settingsRepository.save(settings);
    }

    // ==================== Holiday Management ====================

    @Transactional
    public SchoolYearSettingsResponse addHoliday(AddHolidayRequest request, Long schoolId) {
        SchoolYearSettings settings = settingsRepository.findBySchoolIdAndIsActiveTrue(schoolId)
            .orElseThrow(() -> new RuntimeException("No active school year settings found"));

        validateHolidayDate(request.getDate(), settings.getStartDate(), settings.getEndDate());

        Holiday holiday = new Holiday(request.getName(), request.getDate(),
                                      request.getDescription(), settings);
        settings.addHoliday(holiday);

        settings = settingsRepository.save(settings);
        return new SchoolYearSettingsResponse(settings);
    }

    @Transactional
    public void removeHoliday(Long holidayId, Long schoolId) {
        SchoolYearSettings settings = settingsRepository.findBySchoolIdAndIsActiveTrue(schoolId)
            .orElseThrow(() -> new RuntimeException("No active school year settings found"));

        Holiday holiday = holidayRepository.findByIdAndSchoolYearSettingsId(holidayId, settings.getId())
            .orElseThrow(() -> new RuntimeException("Holiday not found"));

        settings.removeHoliday(holiday);
        settingsRepository.save(settings);
    }

    // ==================== Break Period Management ====================

    @Transactional
    public SchoolYearSettingsResponse addBreakPeriod(AddBreakPeriodRequest request, Long schoolId) {
        SchoolYearSettings settings = settingsRepository.findBySchoolIdAndIsActiveTrue(schoolId)
            .orElseThrow(() -> new RuntimeException("No active school year settings found"));

        validateBreakDates(request.getStartDate(), request.getEndDate(),
                          settings.getStartDate(), settings.getEndDate());

        BreakPeriod breakPeriod = new BreakPeriod(request.getName(),
            BreakType.getFromString(request.getBreakType()),
            request.getStartDate(), request.getEndDate(),
            request.getDescription(), settings);
        settings.addBreakPeriod(breakPeriod);

        settings = settingsRepository.save(settings);
        return new SchoolYearSettingsResponse(settings);
    }

    @Transactional
    public void removeBreakPeriod(Long breakPeriodId, Long schoolId) {
        SchoolYearSettings settings = settingsRepository.findBySchoolIdAndIsActiveTrue(schoolId)
            .orElseThrow(() -> new RuntimeException("No active school year settings found"));

        BreakPeriod breakPeriod = breakPeriodRepository.findByIdAndSchoolYearSettingsId(breakPeriodId, settings.getId())
            .orElseThrow(() -> new RuntimeException("Break period not found"));

        settings.removeBreakPeriod(breakPeriod);
        settingsRepository.save(settings);
    }

    // ==================== Schedule Period Management ====================

    @Transactional
    public SchoolYearSettingsResponse addSchedulePeriod(AddSchedulePeriodRequest request, Long schoolId) {
        SchoolYearSettings settings = settingsRepository.findBySchoolIdAndIsActiveTrue(schoolId)
            .orElseThrow(() -> new RuntimeException("No active school year settings found"));

        validateSchedulePeriodTimes(request.getStartTime(), request.getEndTime(),
                                    settings.getSchoolDayStart(), settings.getSchoolDayEnd());

        SchedulePeriod period = new SchedulePeriod(request.getName(),
            request.getPeriodNumber(),
            PeriodType.getFromString(request.getPeriodType()),
            request.getStartTime(), request.getEndTime(), settings);
        settings.addSchedulePeriod(period);

        settings = settingsRepository.save(settings);
        return new SchoolYearSettingsResponse(settings);
    }

    @Transactional
    public void removeSchedulePeriod(Long periodId, Long schoolId) {
        SchoolYearSettings settings = settingsRepository.findBySchoolIdAndIsActiveTrue(schoolId)
            .orElseThrow(() -> new RuntimeException("No active school year settings found"));

        SchedulePeriod period = schedulePeriodRepository.findByIdAndSchoolYearSettingsId(periodId, settings.getId())
            .orElseThrow(() -> new RuntimeException("Schedule period not found"));

        settings.removeSchedulePeriod(period);
        settingsRepository.save(settings);
    }

    // ==================== Utility Methods ====================

    public boolean isSchoolDay(Long schoolId, LocalDate date) {
        SchoolYearSettings settings = settingsRepository.findBySchoolIdAndIsActiveTrue(schoolId)
            .orElse(null);
        if (settings == null) return false;

        // If dates aren't configured yet, we can't determine if it's a school day
        if (settings.getStartDate() == null || settings.getEndDate() == null) {
            return false;
        }

        // Check if within school year
        if (date.isBefore(settings.getStartDate()) || date.isAfter(settings.getEndDate())) {
            return false;
        }

        // Check if it's a holiday
        if (holidayRepository.findBySchoolYearSettingsIdAndDate(settings.getId(), date).isPresent()) {
            return false;
        }

        // Check if it's during a break
        if (breakPeriodRepository.findBySchoolYearSettingsIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                settings.getId(), date, date).isPresent()) {
            return false;
        }

        return true;
    }

    /**
     * Checks if the school year settings are fully configured (has dates set)
     */
    public boolean isConfigured(SchoolYearSettings settings) {
        return settings.getStartDate() != null &&
               settings.getEndDate() != null &&
               settings.getSchoolDayStart() != null &&
               settings.getSchoolDayEnd() != null;
    }

    public SchedulePeriod getCurrentPeriod(Long schoolId, LocalTime time) {
        SchoolYearSettings settings = settingsRepository.findBySchoolIdAndIsActiveTrue(schoolId)
            .orElseThrow(() -> new RuntimeException("No active school year settings"));

        return schedulePeriodRepository.findBySchoolYearSettingsIdAndStartTimeLessThanEqualAndEndTimeGreaterThan(
                settings.getId(), time, time)
            .orElse(null);
    }

    // ==================== Validation Methods ====================

    private void validateSchoolYearDates(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
    }

    private void validateTermDates(LocalDate termStart, LocalDate termEnd,
                                   LocalDate yearStart, LocalDate yearEnd) {
        if (termEnd.isBefore(termStart)) {
            throw new IllegalArgumentException("Term end date must be after start date");
        }
        if (termStart.isBefore(yearStart) || termEnd.isAfter(yearEnd)) {
            throw new IllegalArgumentException("Term dates must be within the school year");
        }
    }

    private void validateHolidayDate(LocalDate holidayDate, LocalDate yearStart, LocalDate yearEnd) {
        if (holidayDate.isBefore(yearStart) || holidayDate.isAfter(yearEnd)) {
            throw new IllegalArgumentException("Holiday date must be within the school year");
        }
    }

    private void validateBreakDates(LocalDate breakStart, LocalDate breakEnd,
                                    LocalDate yearStart, LocalDate yearEnd) {
        if (breakEnd.isBefore(breakStart)) {
            throw new IllegalArgumentException("Break end date must be after start date");
        }
        if (breakStart.isBefore(yearStart) || breakEnd.isAfter(yearEnd)) {
            throw new IllegalArgumentException("Break dates must be within the school year");
        }
    }

    private void validateSchedulePeriodTimes(LocalTime periodStart, LocalTime periodEnd,
                                             LocalTime dayStart, LocalTime dayEnd) {
        if (periodEnd.isBefore(periodStart)) {
            throw new IllegalArgumentException("Period end time must be after start time");
        }
        if (periodStart.isBefore(dayStart) || periodEnd.isAfter(dayEnd)) {
            throw new IllegalArgumentException("Period times must be within school day hours");
        }
    }

    private void deactivateCurrentSettings(Long schoolId) {
        settingsRepository.findBySchoolIdAndIsActiveTrue(schoolId)
            .ifPresent(settings -> {
                settings.setActive(false);
                settingsRepository.save(settings);
            });
    }
}
