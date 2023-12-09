package com.example.inkzone.service;

import com.example.inkzone.model.dto.view.TaskViewModel;
import com.example.inkzone.model.entity.Task;

import java.util.List;

public interface TaskService {
    List<TaskViewModel> getAllTasks();

    public Task createTask();

    void deleteTask(Long id);
}
