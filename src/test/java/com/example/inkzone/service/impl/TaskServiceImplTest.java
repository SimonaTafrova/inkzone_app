package com.example.inkzone.service.impl;

import com.example.inkzone.model.dto.view.ItemViewModel;
import com.example.inkzone.model.dto.view.TaskViewModel;
import com.example.inkzone.model.entity.Task;
import com.example.inkzone.repository.TaskRepository;
import com.example.inkzone.service.EmailService;
import com.example.inkzone.service.ItemService;
import com.example.inkzone.service.TaskService;
import com.example.inkzone.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ItemService itemService;
    @Mock
    private EmailService emailService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private UserService userService;
    @InjectMocks
    private TaskServiceImpl taskService;
    @Captor
    private ArgumentCaptor<Task> taskArgumentCaptor;
    @Captor
    private ArgumentCaptor<List<TaskViewModel>> taskListArgumentCaptor;


    @Test
    public void createNewTask_noItemsLowOnQuantity_newTaskIsCreatedSuccessfully(){
        when(itemService.getLowQuantityItems()).thenReturn(new ArrayList<>());

        Task task = new Task();
        task.setDate(LocalDate.now());
        task.setContent("There are no items you need to purchase today!");

        taskService.createTask();

        verify(taskRepository).save(taskArgumentCaptor.capture());
        Task returnedTask = taskArgumentCaptor.getValue();
        Assertions.assertEquals(task.getContent(), returnedTask.getContent());
        Assertions.assertEquals(task.getDate(), returnedTask.getDate());

    }

    @Test
    public void createNesTask_withLowQuantityItems_TaskIsCreatedSuccessfully(){
        when(itemService.getLowQuantityItems()).thenReturn(List.of(createItemViewModel("Needle", 9, 10),
                createItemViewModel("Ink", 1 , 2)));

        Task task = new Task();
        task.setDate(LocalDate.now());
        task.setContent(String.format("The following items need to be ordered:%nNeedle%nInk%n"));

        taskService.createTask();
        verify(taskRepository).save(taskArgumentCaptor.capture());
        Task returnedTask = taskArgumentCaptor.getValue();
        Assertions.assertEquals(task.getContent(), returnedTask.getContent());
        Assertions.assertEquals(task.getDate(), returnedTask.getDate());
    }

    @Test
    public void getAllTasks_NoTaskPresent_ReturnsAnEmptyList(){
        when(taskRepository.findAllByOrderByDateAsc()).thenReturn(new ArrayList<>());
        List< TaskViewModel> taskViewModels = taskService.getAllTasks();

        Assertions.assertEquals(0, taskViewModels.size());

    }

    @Test
    public void getAllTask_withPresentTasks_ReturnsANonEmptyList(){


        doReturn(List.of(createTask("Test Task1", LocalDate.now()),
                createTask("Test Task 2", LocalDate.now()))).when(taskRepository).findAllByOrderByDateAsc();

        List<TaskViewModel> taskViewModels = taskService.getAllTasks();


        Assertions.assertEquals(2, taskViewModels.size());


    }

    @Test
    public void deleteById(){
        taskService.deleteTask(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    private Task createTask(String content, LocalDate date) {
        Task task = new Task();
        task.setContent(content);
        task.setDate(date);
        return task;
    }

    private ItemViewModel createItemViewModel(String name, int quantity, int minQuantity) {
        ItemViewModel itemViewModel = new ItemViewModel();
        itemViewModel.setName(name);
        itemViewModel.setQuantity(quantity);
        itemViewModel.setMinQuantity(minQuantity);
        return itemViewModel;
    }


}