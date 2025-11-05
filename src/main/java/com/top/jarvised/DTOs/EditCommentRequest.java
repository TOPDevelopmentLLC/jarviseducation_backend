package com.top.jarvised.DTOs;

public class EditCommentRequest {
    private String bodyText;

    public EditCommentRequest() {}

    public EditCommentRequest(String bodyText) {
        this.bodyText = bodyText;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }
}
