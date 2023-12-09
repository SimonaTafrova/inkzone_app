package com.example.inkzone.model.dto.view;

import java.util.List;

public class CalendarDayViewModel {
    private Long id;
    private Integer day;
    private Integer month;
    private Integer year;
    private List<EventViewModel> events;

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<EventViewModel> getEvents() {
        return events;
    }

    public void setEvents(List<EventViewModel> events) {
        this.events = events;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CalendarDayViewModel() {
    }
}
