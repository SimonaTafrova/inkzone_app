package com.example.inkzone.model.dto.view;

import javax.persistence.Column;
import java.time.LocalDate;

public class TaskViewModel {
    private Long id;

    private LocalDate date;

    private String content;

    public TaskViewModel() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
