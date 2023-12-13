package com.example.inkzone.model.entity;


import com.example.inkzone.service.CalendarService;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "events")
public class Event extends BaseEntity{

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String time;
    @ManyToOne
    private UserEntity creator;




    public Event() {
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }




    public UserEntity getCreator() {
        return creator;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public void setCreator(UserEntity creator) {
        this.creator = creator;
    }


}