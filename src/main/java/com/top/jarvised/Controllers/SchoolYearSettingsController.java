package com.top.jarvised.Controllers;

import com.top.jarvised.DTOs.*;
import com.top.jarvised.Entities.SchedulePeriod;
import com.top.jarvised.JwtUtil;
import com.top.jarvised.Services.SchoolYearSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/school-year-settings")
public class SchoolYearSettingsController {

    private final SchoolYearSettingsService settingsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public SchoolYearSettingsController(SchoolYearSettingsService settingsService, JwtUtil jwtUtil) {
        this.settingsService = settingsService;
        this.jwtUtil = jwtUtil;
    }

    // ==================== Main Settings Endpoints ====================

    /**
     * Get all historical (inactive) school year settings for the school
     */
    @GetMapping("/historical")
    public ResponseEntity<?> getHistoricalSettings(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            List<SchoolYearSettingsResponse> settings = settingsService.getHistoricalSettings(schoolId);
            return ResponseEntity.ok(Map.of("settings", settings));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to fetch historical settings: " + e.getMessage()));
        }
    }

    /**
     * Get the currently active school year settings
     */
    @GetMapping("/active")
    public ResponseEntity<?> getActiveSettings(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            SchoolYearSettingsResponse settings = settingsService.getActiveSettings(schoolId);
            return ResponseEntity.ok(Map.of("settings", settings));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get specific school year settings by ID
     */
    @GetMapping("/{settingsId}")
    public ResponseEntity<?> getSettingsById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long settingsId) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            SchoolYearSettingsResponse settings = settingsService.getSettingsById(settingsId, schoolId);
            return ResponseEntity.ok(Map.of("settings", settings));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Update school year settings (only active settings can be updated)
     */
    @PutMapping("/{settingsId}")
    public ResponseEntity<?> updateSettings(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long settingsId,
            @RequestBody UpdateSchoolYearSettingsRequest request) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            SchoolYearSettingsResponse settings = settingsService.updateSettings(settingsId, request, schoolId);
            return ResponseEntity.ok(Map.of("settings", settings));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update settings: " + e.getMessage()));
        }
    }

    // ==================== Term Endpoints ====================

    @PostMapping("/{settingsId}/terms")
    public ResponseEntity<?> addTerm(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long settingsId,
            @RequestBody AddTermRequest request) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            SchoolYearSettingsResponse settings = settingsService.addTerm(settingsId, request, schoolId);
            return ResponseEntity.ok(Map.of("settings", settings));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to add term: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{settingsId}/terms/{termId}")
    public ResponseEntity<?> removeTerm(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long settingsId,
            @PathVariable Long termId) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            settingsService.removeTerm(settingsId, termId, schoolId);
            return ResponseEntity.ok(Map.of("message", "Term removed successfully"));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to remove term: " + e.getMessage()));
        }
    }

    // ==================== Holiday Endpoints ====================

    @PostMapping("/{settingsId}/holidays")
    public ResponseEntity<?> addHoliday(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long settingsId,
            @RequestBody AddHolidayRequest request) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            SchoolYearSettingsResponse settings = settingsService.addHoliday(settingsId, request, schoolId);
            return ResponseEntity.ok(Map.of("settings", settings));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to add holiday: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{settingsId}/holidays/{holidayId}")
    public ResponseEntity<?> removeHoliday(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long settingsId,
            @PathVariable Long holidayId) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            settingsService.removeHoliday(settingsId, holidayId, schoolId);
            return ResponseEntity.ok(Map.of("message", "Holiday removed successfully"));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to remove holiday: " + e.getMessage()));
        }
    }

    // ==================== Break Period Endpoints ====================

    @PostMapping("/{settingsId}/breaks")
    public ResponseEntity<?> addBreakPeriod(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long settingsId,
            @RequestBody AddBreakPeriodRequest request) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            SchoolYearSettingsResponse settings = settingsService.addBreakPeriod(settingsId, request, schoolId);
            return ResponseEntity.ok(Map.of("settings", settings));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to add break period: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{settingsId}/breaks/{breakId}")
    public ResponseEntity<?> removeBreakPeriod(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long settingsId,
            @PathVariable Long breakId) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            settingsService.removeBreakPeriod(settingsId, breakId, schoolId);
            return ResponseEntity.ok(Map.of("message", "Break period removed successfully"));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to remove break period: " + e.getMessage()));
        }
    }

    // ==================== Schedule Period Endpoints ====================

    @PostMapping("/{settingsId}/periods")
    public ResponseEntity<?> addSchedulePeriod(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long settingsId,
            @RequestBody AddSchedulePeriodRequest request) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            SchoolYearSettingsResponse settings = settingsService.addSchedulePeriod(settingsId, request, schoolId);
            return ResponseEntity.ok(Map.of("settings", settings));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to add schedule period: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{settingsId}/periods/{periodId}")
    public ResponseEntity<?> removeSchedulePeriod(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long settingsId,
            @PathVariable Long periodId) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            settingsService.removeSchedulePeriod(settingsId, periodId, schoolId);
            return ResponseEntity.ok(Map.of("message", "Schedule period removed successfully"));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to remove schedule period: " + e.getMessage()));
        }
    }

    // ==================== Utility Endpoints ====================

    /**
     * Check if a specific date is a school day
     */
    @GetMapping("/is-school-day")
    public ResponseEntity<?> isSchoolDay(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String date) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            LocalDate queryDate = LocalDate.parse(date);
            boolean isSchoolDay = settingsService.isSchoolDay(schoolId, queryDate);
            return ResponseEntity.ok(Map.of("isSchoolDay", isSchoolDay, "date", date));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to check school day: " + e.getMessage()));
        }
    }

    /**
     * Get the current period based on time
     */
    @GetMapping("/current-period")
    public ResponseEntity<?> getCurrentPeriod(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) String time) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            LocalTime queryTime = (time != null) ? LocalTime.parse(time) : LocalTime.now();
            SchedulePeriod period = settingsService.getCurrentPeriod(schoolId, queryTime);

            if (period != null) {
                return ResponseEntity.ok(Map.of(
                    "period", Map.of(
                        "id", period.getId(),
                        "name", period.getName(),
                        "periodNumber", period.getPeriodNumber(),
                        "periodType", period.getPeriodType().toString(),
                        "startTime", period.getStartTime().toString(),
                        "endTime", period.getEndTime().toString()
                    ),
                    "time", queryTime.toString()
                ));
            } else {
                return ResponseEntity.ok(Map.of("period", (Object) null, "time", queryTime.toString()));
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to get current period: " + e.getMessage()));
        }
    }
}
