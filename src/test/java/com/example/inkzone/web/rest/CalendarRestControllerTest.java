package com.example.inkzone.web.rest;

import com.example.inkzone.model.dto.binding.CalendarDayBindingModel;
import com.example.inkzone.model.dto.binding.EventBindingModel;
import com.example.inkzone.model.dto.view.CalendarDayViewModel;
import com.example.inkzone.model.dto.view.EventViewModel;
import com.example.inkzone.service.CalendarService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CalendarRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CalendarService calendarService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser("test@example.com")
    public void  getAllEvents_withAuthenticatedUser_listOfEventsIsReturned() throws Exception {

        when(calendarService.getAllDays()).thenReturn(List.of(
                createNewDay(1,12,2023),
                createNewDay(2,12,2023)));

        mockMvc.perform(get("/api/calendar").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].day", Matchers.is(1)))
                .andExpect(jsonPath("$.[1].day", Matchers.is(2)))
                .andExpect(jsonPath("$.[0].events.[0].userName", Matchers.is("test@example.com")));

    }

    @Test
    public void getAllEvents_anonymousUser_redirectsToLoginPage() throws Exception {
        mockMvc.perform(get("/api/calendar"))
                .andExpect(status().is3xxRedirection());
    }


    @Test
    @WithMockUser(username = "test@example.com")
    public void addEvent_withAuthenticatedUser_EventAddedSuccessfully() throws Exception {
        CalendarDayBindingModel calendarDayBindingModel = createCalendarDayBindingModel(1,12,2023);
       when(calendarService.addNewEvent(any(), eq("test@example.com"))).thenReturn(createNewDay(1,12,2023));

       mockMvc.perform(post("/api/calendar/add")
                       .content(objectMapper.writeValueAsString(calendarDayBindingModel))
                       .with(csrf())
                       .contentType("application/json")
                       .accept("application/json"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.day", Matchers.is(1)))
               .andExpect(jsonPath("$.month", Matchers.is(12)))
               .andExpect(jsonPath("$.year", Matchers.is(2023)));

    }

    private CalendarDayBindingModel createCalendarDayBindingModel(int i, int i1, int i2) {
        CalendarDayBindingModel calendarDayBindingModel = new CalendarDayBindingModel();
        calendarDayBindingModel.setDay(i);
        calendarDayBindingModel.setMonth(i1);
        calendarDayBindingModel.setYear(i2);
        EventBindingModel eventBindingModel = new EventBindingModel();
        eventBindingModel.setUserName("test@example.com");

        eventBindingModel.setTitle("test");
        eventBindingModel.setTime("10:00");
        calendarDayBindingModel.setEvents(List.of(eventBindingModel));

        return calendarDayBindingModel;
    }

    private CalendarDayViewModel createNewDay(int i, int i1, int i2) {
        CalendarDayViewModel calendarDayViewModel = new CalendarDayViewModel();
        calendarDayViewModel.setDay(i);
        calendarDayViewModel.setMonth(i1);
        calendarDayViewModel.setYear(i2);

        EventViewModel eventViewModel = new EventViewModel();
        eventViewModel.setUserName("test@example.com");
        eventViewModel.setCreatorId("1");
        eventViewModel.setTitle("test");
        eventViewModel.setTime("10:00");
        calendarDayViewModel.setEvents(List.of(eventViewModel));


        return calendarDayViewModel;
    }

}