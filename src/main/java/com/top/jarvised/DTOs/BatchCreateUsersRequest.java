package com.top.jarvised.DTOs;

import java.util.List;

public class BatchCreateUsersRequest {
    private List<CreateUserRequest> users;

    public BatchCreateUsersRequest() {}

    public BatchCreateUsersRequest(List<CreateUserRequest> users) {
        this.users = users;
    }

    public List<CreateUserRequest> getUsers() {
        return users;
    }

    public void setUsers(List<CreateUserRequest> users) {
        this.users = users;
    }
}
