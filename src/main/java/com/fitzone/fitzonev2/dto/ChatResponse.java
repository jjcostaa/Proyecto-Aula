package com.fitzone.fitzonev2.dto;

public class ChatResponse {
    private String reply;
    private boolean success;

    public ChatResponse() {}

    public ChatResponse(String reply, boolean success) {
        this.reply = reply;
        this.success = success;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
