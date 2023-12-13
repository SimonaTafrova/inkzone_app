package com.example.inkzone.model.dto.binding;

import javax.validation.constraints.NotNull;

public class EventBindingModel {

    @NotNull
    private String title;
    @NotNull
    private String time;
    private String userName;

    public EventBindingModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}