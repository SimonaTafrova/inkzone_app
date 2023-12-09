package com.example.inkzone.service.impl;

import com.example.inkzone.model.dto.binding.CalendarDayBindingModel;
import com.example.inkzone.model.dto.binding.EventBindingModel;
import com.example.inkzone.model.dto.view.CalendarDayViewModel;
import com.example.inkzone.model.dto.view.EventViewModel;
import com.example.inkzone.model.entity.CalendarDay;
import com.example.inkzone.model.entity.Event;
import com.example.inkzone.model.entity.UserEntity;
import com.example.inkzone.repository.CalendarRepository;
import com.example.inkzone.repository.EventRepository;
import com.example.inkzone.service.CalendarService;
import com.example.inkzone.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CalendarServiceImpl implements CalendarService {
    private final EventRepository eventRepository;
    private final CalendarRepository calendarRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public CalendarServiceImpl(EventRepository eventRepository, CalendarRepository calendarRepository, ModelMapper modelMapper, UserService userService) {
        this.eventRepository = eventRepository;
        this.calendarRepository = calendarRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @Override
    public List<CalendarDayViewModel> getAllDays() {
        List<CalendarDayViewModel> allDaysViewModel = new ArrayList<>();
        List<CalendarDay> allDays = calendarRepository.findAll();
        if(!allDays.isEmpty()){
            allDays.forEach(day -> {
                CalendarDayViewModel currentDay = modelMapper.map(day,CalendarDayViewModel.class);
                List<Event> eventsForDay = day.getEvents().stream().toList();
                List<EventViewModel> eventViewModels = new ArrayList<>();
                eventsForDay.forEach(e -> {
                    getEventsForDay(e, eventViewModels);
                });
                currentDay.setEvents(eventViewModels);

                allDaysViewModel.add(currentDay);
            });
        }
        return allDaysViewModel;
    }

    private void getEventsForDay(Event e, List<EventViewModel> eventViewModels) {
        EventViewModel eventViewModel = modelMapper.map(e, EventViewModel.class);
        UserEntity creator = e.getCreator();
        String id = creator.getId().toString();
        eventViewModel.setUserName(creator.getFirstName());

        eventViewModel.setCreatorId(id);

        eventViewModels.add(eventViewModel);
    }

    @Override
    public void deleteEvent(Long dayId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);

        CalendarDay calendarDay = calendarRepository.findById(dayId).orElse(null);
        assert calendarDay != null;

        Set<Event> updated = new HashSet<>();

        calendarDay.getEvents().forEach(e -> {
            if(e.getId() != eventId){
                updated.add(e);
            }
        });
        assert event != null;

        calendarDay.getEvents().clear();
        calendarDay.setEvents(updated);












        if(calendarDay.getEvents().isEmpty()){
            calendarRepository.delete(calendarDay);
        } else {
            calendarRepository.save(calendarDay);
        }



    }

    @Override
    public CalendarDayViewModel addNewEvent(CalendarDayBindingModel calendarDayBindingModel, String email) {
        CalendarDay existingDay = calendarRepository.findByDayAndMonthAndYear(calendarDayBindingModel.getDay(),
                calendarDayBindingModel.getMonth(), calendarDayBindingModel.getYear()).orElse(null);

        if(existingDay != null){
            Event event = createEvent(calendarDayBindingModel, email);


            Set<Event> currentEvents = existingDay.getEvents();
            currentEvents.add(event);
            existingDay.setEvents(currentEvents);
            calendarRepository.save(existingDay);

            return modelMapper.map(existingDay, CalendarDayViewModel.class);

        }

        CalendarDay newDay = modelMapper.map(calendarDayBindingModel, CalendarDay.class);
        Event event = createEvent(calendarDayBindingModel, email);


        Set<Event> newEventSet = new HashSet<>();
        newEventSet.add(event);
        newDay.setEvents(newEventSet);

        calendarRepository.save(newDay);
        return modelMapper.map(newDay, CalendarDayViewModel.class);


    }

    private Event createEvent(CalendarDayBindingModel calendarDayBindingModel, String email) {
        Event event = new Event();

        UserEntity user = userService.getCreator(email);
        event.setCreator(user);

        List<EventBindingModel> events = calendarDayBindingModel.getEvents();
        event.setTitle(events.get(0).getTitle());
        event.setTime(events.get(0).getTime());
        eventRepository.save(event);
        return event;
    }
}
