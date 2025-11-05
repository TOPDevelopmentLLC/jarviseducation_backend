package com.top.jarvised.DTOs;

import java.util.List;

public class CreateTeamRequest {
    private String name;
    private String description;
    private List<Long> memberIds;
    private List<Long> codeIds;

    public CreateTeamRequest() {}

    public CreateTeamRequest(String name, String description, List<Long> memberIds, List<Long> codeIds) {
        this.name = name;
        this.description = description;
        this.memberIds = memberIds;
        this.codeIds = codeIds;
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

    public List<Long> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<Long> memberIds) {
        this.memberIds = memberIds;
    }

    public List<Long> getCodeIds() {
        return codeIds;
    }

    public void setCodeIds(List<Long> codeIds) {
        this.codeIds = codeIds;
    }
}
