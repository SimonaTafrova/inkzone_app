package com.example.inkzone.service.impl;

import com.example.inkzone.model.dto.view.ItemViewModel;
import com.example.inkzone.model.dto.view.TaskViewModel;
import com.example.inkzone.model.entity.Task;
import com.example.inkzone.repository.TaskRepository;
import com.example.inkzone.service.EmailService;
import com.example.inkzone.service.ItemService;
import com.example.inkzone.service.TaskService;
import com.example.inkzone.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
   private  final TaskRepository taskRepository;
    private final EmailService emailService;

    private final ModelMapper modelMapper;
    private final ItemService itemService;
    private final UserService userService;

    public TaskServiceImpl(TaskRepository taskRepository, EmailService emailService, ModelMapper modelMapper, ItemService itemService, UserService userService) {
        this.taskRepository = taskRepository;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
        this.itemService = itemService;
        this.userService = userService;
    }


    @Override
    public List<TaskViewModel> getAllTasks() {
        List<Task> allTasks = taskRepository.findAllByOrderByDateAsc();
        List<TaskViewModel> taskViewModels = new ArrayList<>();
        if(allTasks.isEmpty()){
            return taskViewModels;
        }

        allTasks.forEach(t -> {
            TaskViewModel taskViewModel = modelMapper.map(t, TaskViewModel.class);
            taskViewModels.add(taskViewModel);
        });

        return taskViewModels;


    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public Task createTask(){
        List<ItemViewModel> lowItems = itemService.getLowQuantityItems();
        String message = generateMessage(lowItems);
        Task task = new Task();
        task.setContent(message);
        task.setDate(LocalDate.now());
        taskRepository.save(task);
        return task;
    }

    private String generateMessage(List<ItemViewModel> lowItems) {
        StringBuilder sb = new StringBuilder();
        if(lowItems.isEmpty()){
            sb.append("There are no items you need to purchase today!");
            return sb.toString();
        }
        sb.append("The following items need to be ordered:");
        sb.append(System.lineSeparator());
        lowItems.forEach(i -> {
            sb.append("*").append(i.getName());
            sb.append(System.lineSeparator());
        });
        return sb.toString();
    }

    @Scheduled(cron = "0 00 19 * * ?")
    public void sendTaskEmail() {
        String [] userEmails = userService.getAllAdminsAndModerators();
        Task dailyTask = createTask();
        System.out.println(Arrays.toString(userEmails));
        emailService.sendEmailToMultipleRecipients(userEmails,"Tasks for today ", dailyTask.getContent());

    }




}
