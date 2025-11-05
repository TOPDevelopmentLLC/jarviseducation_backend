package com.top.jarvised.DTOs;

public class AddCommentRequest {
    private String fullName;
    private String bodyText;

    public AddCommentRequest() {}

    public AddCommentRequest(String fullName, String bodyText) {
        this.fullName = fullName;
        this.bodyText = bodyText;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }
}
