package com.top.jarvised.Services;

import com.top.jarvised.Entities.SchoolYearSettings;
import com.top.jarvised.Repositories.SchoolYearSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service responsible for automatic school year rollover.
 * Runs a scheduled task daily to check if any active school years have ended
 * and creates new school year settings for the next year.
 */
@Service
public class SchoolYearRolloverService {

    private static final Logger logger = LoggerFactory.getLogger(SchoolYearRolloverService.class);

    private final SchoolYearSettingsRepository settingsRepository;

    @Autowired
    public SchoolYearRolloverService(SchoolYearSettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    /**
     * Scheduled task that runs daily at midnight to check for school year rollovers.
     * Cron expression: 0 0 0 * * * = every day at 00:00:00
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void checkAndRolloverSchoolYears() {
        logger.info("Running school year rollover check...");

        LocalDate today = LocalDate.now();

        // Find all active school years that have ended (end date is before today)
        List<SchoolYearSettings> allActiveSettings = settingsRepository.findAll().stream()
            .filter(SchoolYearSettings::isActive)
            .filter(settings -> settings.getEndDate() != null)
            .filter(settings -> settings.getEndDate().isBefore(today))
            .toList();

        for (SchoolYearSettings expiredSettings : allActiveSettings) {
            logger.info("School year '{}' for school {} has ended. Creating new year...",
                expiredSettings.getName(), expiredSettings.getSchoolId());

            // Deactivate the expired school year
            expiredSettings.setActive(false);
            settingsRepository.save(expiredSettings);

            // Create a new school year for this school
            createNextSchoolYear(expiredSettings);
        }

        if (allActiveSettings.isEmpty()) {
            logger.info("No school years require rollover.");
        } else {
            logger.info("Rolled over {} school year(s).", allActiveSettings.size());
        }
    }

    /**
     * Creates the next school year settings based on the expired year.
     * The new year is created with null dates (to be configured by admin) and set as active.
     *
     * @param expiredSettings The school year that just ended
     */
    private void createNextSchoolYear(SchoolYearSettings expiredSettings) {
        // Calculate next year's name
        LocalDate expiredEndDate = expiredSettings.getEndDate();
        int nextStartYear = expiredEndDate.getYear();
        // If the year ended in the first half of the calendar year, use that year as start
        // Otherwise, use next year as start (typical school year ends in May-June)
        if (expiredEndDate.getMonthValue() <= 6) {
            nextStartYear = expiredEndDate.getYear();
        } else {
            nextStartYear = expiredEndDate.getYear() + 1;
        }
        String nextYearName = nextStartYear + "-" + (nextStartYear + 1) + " School Year";

        SchoolYearSettings newSettings = new SchoolYearSettings();
        newSettings.setName(nextYearName);
        newSettings.setSchoolId(expiredSettings.getSchoolId());
        newSettings.setActive(true);  // New year is active
        newSettings.setTermType(expiredSettings.getTermType());  // Carry over term type
        newSettings.setTimezone(expiredSettings.getTimezone());  // Carry over timezone
        // Leave dates as null - to be configured by admin
        newSettings.setStartDate(null);
        newSettings.setEndDate(null);
        newSettings.setSchoolDayStart(null);
        newSettings.setSchoolDayEnd(null);

        settingsRepository.save(newSettings);

        logger.info("Created new school year '{}' for school {}",
            nextYearName, expiredSettings.getSchoolId());
    }

    /**
     * Manual trigger for rollover check (useful for testing or admin override)
     */
    @Transactional
    public int manualRolloverCheck() {
        logger.info("Manual rollover check triggered");

        LocalDate today = LocalDate.now();
        int rolledOver = 0;

        List<SchoolYearSettings> allActiveSettings = settingsRepository.findAll().stream()
            .filter(SchoolYearSettings::isActive)
            .filter(settings -> settings.getEndDate() != null)
            .filter(settings -> settings.getEndDate().isBefore(today))
            .toList();

        for (SchoolYearSettings expiredSettings : allActiveSettings) {
            expiredSettings.setActive(false);
            settingsRepository.save(expiredSettings);
            createNextSchoolYear(expiredSettings);
            rolledOver++;
        }

        return rolledOver;
    }
}
