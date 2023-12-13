package com.example.inkzone.service.impl;

import com.example.inkzone.model.entity.CalendarDay;
import com.example.inkzone.model.entity.Event;
import com.example.inkzone.model.entity.Picture;
import com.example.inkzone.model.entity.UserEntity;
import com.example.inkzone.repository.CalendarRepository;
import com.example.inkzone.repository.EventRepository;
import com.example.inkzone.repository.PictureRepository;
import com.example.inkzone.service.DeleteService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DeleteServiceImpl implements DeleteService {
    private final PictureRepository pictureRepository;
    private final EventRepository eventRepository;
    private final CalendarRepository calendarRepository;


    public DeleteServiceImpl(PictureRepository pictureRepository, EventRepository eventRepository, CalendarRepository calendarRepository) {
        this.pictureRepository = pictureRepository;

        this.eventRepository = eventRepository;
        this.calendarRepository = calendarRepository;
    }

    public void getAllPicturesOfUserAndDelete(String email) {
        List<Picture> picturesToDelete = pictureRepository.getAllByCreator_Email(email).orElse(null);
        if(picturesToDelete != null){
            pictureRepository.deleteAll(picturesToDelete);
        }

    }


    public void deleteEvents(UserEntity userEntity) {
        List<Event> events = eventRepository.getAllByCreator_Email(userEntity.getEmail()).orElse(null);
        assert events != null;
        for(Event event : events){
            CalendarDay calendarDay = calendarRepository.findByEventsContaining(event).orElse(null);
            Set<Event> dayEvents =  calendarDay.getEvents();
            Set<Event> newSet = new HashSet<>();
           for (Event dayEvent: dayEvents){
                if(dayEvent.getId() != event.getId()){
                    newSet.add(dayEvent);
                }
            }
           calendarDay.setEvents(newSet);
           calendarRepository.save(calendarDay);
           eventRepository.delete(event);
        }

    }
}