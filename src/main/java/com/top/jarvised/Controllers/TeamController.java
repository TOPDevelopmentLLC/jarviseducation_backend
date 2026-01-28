package com.top.jarvised.Controllers;

import com.top.jarvised.DTOs.CreateTeamRequest;
import com.top.jarvised.DTOs.TeamResponse;
import com.top.jarvised.DTOs.UpdateTeamRequest;
import com.top.jarvised.JwtUtil;
import com.top.jarvised.SchoolContext;
import com.top.jarvised.Services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private TeamService teamService;
    private JwtUtil jwtUtil;

    @Autowired
    public TeamController(TeamService teamService, JwtUtil jwtUtil) {
        this.teamService = teamService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Get all teams for the authenticated user's school
     */
    @GetMapping
    public ResponseEntity<?> getAllTeams(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            SchoolContext.setSchool(schoolId.toString());
            List<TeamResponse> teams = teamService.getAllTeams(schoolId);
            return ResponseEntity.ok(Map.of("teams", teams));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to fetch teams: " + e.getMessage()));
        }
    }

    /**
     * Get a specific team by ID
     */
    @GetMapping("/{teamId}")
    public ResponseEntity<?> getTeamById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long teamId) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            SchoolContext.setSchool(schoolId.toString());
            TeamResponse team = teamService.getTeamById(teamId, schoolId);
            return ResponseEntity.ok(Map.of("team", team));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to fetch team: " + e.getMessage()));
        }
    }

    /**
     * Get teams for a specific user (teams they are a member of)
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getTeamsForUser(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long userId) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            SchoolContext.setSchool(schoolId.toString());
            List<TeamResponse> teams = teamService.getTeamsForUser(userId, schoolId);
            return ResponseEntity.ok(Map.of("teams", teams));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to fetch user teams: " + e.getMessage()));
        }
    }

    /**
     * Create a new team
     */
    @PostMapping
    public ResponseEntity<?> createTeam(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CreateTeamRequest request) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            SchoolContext.setSchool(schoolId.toString());
            TeamResponse team = teamService.createTeam(request, schoolId);
            return ResponseEntity.ok(Map.of("team", team));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create team: " + e.getMessage()));
        }
    }

    /**
     * Update a team (add/remove members and codes)
     */
    @PutMapping("/{teamId}")
    public ResponseEntity<?> updateTeam(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long teamId,
            @RequestBody UpdateTeamRequest request) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            SchoolContext.setSchool(schoolId.toString());
            TeamResponse team = teamService.updateTeam(teamId, request, schoolId);
            return ResponseEntity.ok(Map.of("team", team));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update team: " + e.getMessage()));
        }
    }

    /**
     * Delete a team
     */
    @DeleteMapping("/{teamId}")
    public ResponseEntity<?> deleteTeam(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long teamId) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long schoolId = jwtUtil.extractSchoolId(token);

            if (schoolId == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid token: missing school ID"));
            }

            SchoolContext.setSchool(schoolId.toString());
            teamService.deleteTeam(teamId, schoolId);
            return ResponseEntity.ok(Map.of("message", "Team deleted successfully"));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to delete team: " + e.getMessage()));
        }
    }
}
