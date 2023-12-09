package com.example.inkzone.web;

import com.example.inkzone.model.dto.view.TaskViewModel;
import com.example.inkzone.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TasksController {
    private final TaskService taskService;

    public TasksController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping()
    public String getTasks(Model model){
        List<TaskViewModel> allTasks = taskService.getAllTasks();
        if(allTasks.isEmpty()){
            model.addAttribute("noTasks", true);
        }else{
            model.addAttribute("tasks", allTasks);
        }

        return "tasks";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") Long id){
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }
}
