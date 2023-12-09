package com.example.inkzone.service;

import com.example.inkzone.model.dto.binding.CalendarDayBindingModel;
import com.example.inkzone.model.dto.view.CalendarDayViewModel;

import java.util.List;

public interface CalendarService {
    List<CalendarDayViewModel> getAllDays();

    void deleteEvent(Long dayId, Long eventId);

    CalendarDayViewModel addNewEvent(CalendarDayBindingModel calendarDayBindingModel, String email);
}
