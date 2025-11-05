package com.top.jarvised.DTOs;

import java.util.List;

public class UpdateTeamRequest {
    private String name;
    private String description;
    private List<Long> addMemberIds;
    private List<Long> removeMemberIds;
    private List<Long> addCodeIds;
    private List<Long> removeCodeIds;

    public UpdateTeamRequest() {}

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

    public List<Long> getAddMemberIds() {
        return addMemberIds;
    }

    public void setAddMemberIds(List<Long> addMemberIds) {
        this.addMemberIds = addMemberIds;
    }

    public List<Long> getRemoveMemberIds() {
        return removeMemberIds;
    }

    public void setRemoveMemberIds(List<Long> removeMemberIds) {
        this.removeMemberIds = removeMemberIds;
    }

    public List<Long> getAddCodeIds() {
        return addCodeIds;
    }

    public void setAddCodeIds(List<Long> addCodeIds) {
        this.addCodeIds = addCodeIds;
    }

    public List<Long> getRemoveCodeIds() {
        return removeCodeIds;
    }

    public void setRemoveCodeIds(List<Long> removeCodeIds) {
        this.removeCodeIds = removeCodeIds;
    }
}
