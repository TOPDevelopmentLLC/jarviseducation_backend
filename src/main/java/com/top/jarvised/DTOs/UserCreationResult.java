package com.top.jarvised.DTOs;

public class UserCreationResult {
    private boolean success;
    private String email;
    private String message;
    private String temporaryPassword;

    public UserCreationResult() {}

    public UserCreationResult(boolean success, String email, String message, String temporaryPassword) {
        this.success = success;
        this.email = email;
        this.message = message;
        this.temporaryPassword = temporaryPassword;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTemporaryPassword() {
        return temporaryPassword;
    }

    public void setTemporaryPassword(String temporaryPassword) {
        this.temporaryPassword = temporaryPassword;
    }
}
