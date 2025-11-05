package com.top.jarvised.Services;

import com.top.jarvised.DTOs.CreateTeamRequest;
import com.top.jarvised.DTOs.TeamResponse;
import com.top.jarvised.DTOs.UpdateTeamRequest;
import com.top.jarvised.Entities.Code;
import com.top.jarvised.Entities.Team;
import com.top.jarvised.Entities.UserAccount;
import com.top.jarvised.Enums.AccountType;
import com.top.jarvised.Repositories.CodeRepository;
import com.top.jarvised.Repositories.TeamRepository;
import com.top.jarvised.Repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private TeamRepository teamRepository;
    private UserAccountRepository userAccountRepository;
    private CodeRepository codeRepository;

    @Autowired
    public TeamService(
            TeamRepository teamRepository,
            UserAccountRepository userAccountRepository,
            CodeRepository codeRepository) {
        this.teamRepository = teamRepository;
        this.userAccountRepository = userAccountRepository;
        this.codeRepository = codeRepository;
    }

    /**
     * Get all teams for a specific school
     */
    public List<TeamResponse> getAllTeams(Long schoolId) {
        List<Team> teams = teamRepository.findBySchoolId(schoolId);
        return teams.stream()
            .map(TeamResponse::new)
            .collect(Collectors.toList());
    }

    /**
     * Get a specific team by ID
     */
    public TeamResponse getTeamById(Long teamId, Long schoolId) {
        Team team = teamRepository.findByIdAndSchoolId(teamId, schoolId)
            .orElseThrow(() -> new RuntimeException("Team not found"));
        return new TeamResponse(team);
    }

    /**
     * Get teams that a user is a member of
     */
    public List<TeamResponse> getTeamsForUser(Long userId, Long schoolId) {
        UserAccount user = userAccountRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify user belongs to the school
        if (!user.getSchoolId().equals(schoolId)) {
            throw new RuntimeException("User does not belong to this school");
        }

        List<Team> allTeams = teamRepository.findBySchoolId(schoolId);
        return allTeams.stream()
            .filter(team -> team.getMembers().contains(user))
            .map(TeamResponse::new)
            .collect(Collectors.toList());
    }

    /**
     * Create a new team
     */
    @Transactional
    public TeamResponse createTeam(CreateTeamRequest request, Long schoolId) {
        // Create new team
        Team team = new Team(request.getName(), request.getDescription(), schoolId);

        // Add members (only Teachers and Administrators)
        if (request.getMemberIds() != null && !request.getMemberIds().isEmpty()) {
            List<UserAccount> members = userAccountRepository.findAllById(request.getMemberIds());
            for (UserAccount member : members) {
                // Validate member belongs to same school
                if (!member.getSchoolId().equals(schoolId)) {
                    throw new RuntimeException("User " + member.getEmail() + " does not belong to this school");
                }
                // Validate member is Teacher or Administrator
                if (member.getAccountType() != AccountType.Teacher &&
                    member.getAccountType() != AccountType.Administrator) {
                    throw new RuntimeException("User " + member.getEmail() + " is not a Teacher or Administrator");
                }
                team.addMember(member);
            }
        }

        // Add codes
        if (request.getCodeIds() != null && !request.getCodeIds().isEmpty()) {
            List<Code> codes = codeRepository.findByIdIn(request.getCodeIds());
            codes.forEach(team::addCode);
        }

        team = teamRepository.save(team);
        return new TeamResponse(team);
    }

    /**
     * Update a team
     */
    @Transactional
    public TeamResponse updateTeam(Long teamId, UpdateTeamRequest request, Long schoolId) {
        Team team = teamRepository.findByIdAndSchoolId(teamId, schoolId)
            .orElseThrow(() -> new RuntimeException("Team not found"));

        // Update basic info
        if (request.getName() != null) {
            team.setName(request.getName());
        }
        if (request.getDescription() != null) {
            team.setDescription(request.getDescription());
        }

        // Add members
        if (request.getAddMemberIds() != null && !request.getAddMemberIds().isEmpty()) {
            List<UserAccount> membersToAdd = userAccountRepository.findAllById(request.getAddMemberIds());
            for (UserAccount member : membersToAdd) {
                // Validate member belongs to same school
                if (!member.getSchoolId().equals(schoolId)) {
                    throw new RuntimeException("User " + member.getEmail() + " does not belong to this school");
                }
                // Validate member is Teacher or Administrator
                if (member.getAccountType() != AccountType.Teacher &&
                    member.getAccountType() != AccountType.Administrator) {
                    throw new RuntimeException("User " + member.getEmail() + " is not a Teacher or Administrator");
                }
                team.addMember(member);
            }
        }

        // Remove members
        if (request.getRemoveMemberIds() != null && !request.getRemoveMemberIds().isEmpty()) {
            List<UserAccount> membersToRemove = userAccountRepository.findAllById(request.getRemoveMemberIds());
            membersToRemove.forEach(team::removeMember);
        }

        // Add codes
        if (request.getAddCodeIds() != null && !request.getAddCodeIds().isEmpty()) {
            List<Code> codesToAdd = codeRepository.findByIdIn(request.getAddCodeIds());
            codesToAdd.forEach(team::addCode);
        }

        // Remove codes
        if (request.getRemoveCodeIds() != null && !request.getRemoveCodeIds().isEmpty()) {
            List<Code> codesToRemove = codeRepository.findByIdIn(request.getRemoveCodeIds());
            codesToRemove.forEach(team::removeCode);
        }

        team = teamRepository.save(team);
        return new TeamResponse(team);
    }

    /**
     * Delete a team
     */
    @Transactional
    public void deleteTeam(Long teamId, Long schoolId) {
        Team team = teamRepository.findByIdAndSchoolId(teamId, schoolId)
            .orElseThrow(() -> new RuntimeException("Team not found"));
        teamRepository.delete(team);
    }
}
