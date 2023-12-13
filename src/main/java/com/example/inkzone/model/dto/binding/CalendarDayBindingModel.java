package com.example.inkzone.model.dto.binding;



import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

public class CalendarDayBindingModel {
    @NotNull
    @Positive
    private Integer day;
    @NotNull
    @Positive
    private Integer month;
    @NotNull
    @Positive
    private Integer year;
    private List<EventBindingModel> events;

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

    public List<EventBindingModel> getEvents() {
        return events;
    }

    public void setEvents(List<EventBindingModel> events) {
        this.events = events;
    }

    public CalendarDayBindingModel() {
    }
}