package com.top.jarvised.DTOs;

import com.top.jarvised.Entities.Team;

import java.util.List;
import java.util.stream.Collectors;

public class TeamResponse {
    private Long id;
    private String name;
    private String description;
    private Long schoolId;
    private List<TeamMemberInfo> members;
    private List<CodeInfo> codes;

    public TeamResponse() {}

    public TeamResponse(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        this.description = team.getDescription();
        this.schoolId = team.getSchoolId();
        this.members = team.getMembers().stream()
            .map(user -> new TeamMemberInfo(user.getId(), user.getEmail(), user.getFullName(), user.getAccountType().toString()))
            .collect(Collectors.toList());
        this.codes = team.getCodes().stream()
            .map(code -> new CodeInfo(code.getId(), code.getCode(), code.getDescription()))
            .collect(Collectors.toList());
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public List<TeamMemberInfo> getMembers() {
        return members;
    }

    public void setMembers(List<TeamMemberInfo> members) {
        this.members = members;
    }

    public List<CodeInfo> getCodes() {
        return codes;
    }

    public void setCodes(List<CodeInfo> codes) {
        this.codes = codes;
    }

    // Nested class for team member info
    public static class TeamMemberInfo {
        private Long id;
        private String email;
        private String fullName;
        private String accountType;

        public TeamMemberInfo(Long id, String email, String fullName, String accountType) {
            this.id = id;
            this.email = email;
            this.fullName = fullName;
            this.accountType = accountType;
        }

        public Long getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getFullName() {
            return fullName;
        }

        public String getAccountType() {
            return accountType;
        }
    }

    // Nested class for code info
    public static class CodeInfo {
        private Long id;
        private String code;
        private String description;

        public CodeInfo(Long id, String code, String description) {
            this.id = id;
            this.code = code;
            this.description = description;
        }

        public Long getId() {
            return id;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }
}
