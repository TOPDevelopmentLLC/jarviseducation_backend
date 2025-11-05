package com.top.jarvised.DTOs;

public class CreateUserRequest {
    private String fullName;
    private String email;
    private String accountType;

    public CreateUserRequest() {}

    public CreateUserRequest(String fullName, String email, String accountType) {
        this.fullName = fullName;
        this.email = email;
        this.accountType = accountType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
