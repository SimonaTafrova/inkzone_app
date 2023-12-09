package com.example.inkzone.model.entity;



import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="calendar_day")
public class CalendarDay extends BaseEntity {

    @Column(nullable = false)
    private Integer day;
    @Column(nullable = false)
    private Integer month;
    @Column(nullable = false)
    private Integer year;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, orphanRemoval = true)
    private Set<Event> events;

    public CalendarDay() {
    }

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

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }
}
