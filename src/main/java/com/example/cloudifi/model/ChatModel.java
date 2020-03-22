package com.example.cloudifi.model;

public class ChatModel {

    private String message;
    private Object sent_time;
    private String who;

    public ChatModel() {
    }

    public ChatModel(String message, Object sent_time, String who) {
        this.message = message;
        this.sent_time = sent_time;
        this.who = who;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getSent_time() {
        return sent_time;
    }

    public void setSent_time(Object sent_time) {
        this.sent_time = sent_time;
    }
}
