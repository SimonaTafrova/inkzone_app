package com.example.inkzone.web.rest;

import com.example.inkzone.model.dto.binding.CalendarDayBindingModel;


import com.example.inkzone.model.dto.view.CalendarDayViewModel;


import com.example.inkzone.service.CalendarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;

import java.util.List;

@RestController
@RequestMapping("/api/calendar")
public class CalendarRestController {
    private final CalendarService calendarService;

    public CalendarRestController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }


    @GetMapping
    public ResponseEntity<List<CalendarDayViewModel>> getAllEvents(){
        List<CalendarDayViewModel> allDays = calendarService.getAllDays();

        return ResponseEntity.ok(allDays);
    }

    @DeleteMapping("/{dayId}/{eventId}")
    public ResponseEntity<Long> deleteEvent(@PathVariable("dayId") Long dayId, @PathVariable("eventId") Long eventId){
        calendarService.deleteEvent(dayId,eventId);
        return  ResponseEntity.noContent().build();
    }

    @PostMapping("/add")
    public ResponseEntity<CalendarDayViewModel> addEvent(@Valid @RequestBody CalendarDayBindingModel calendarDayBindingModel,
                                                         UriComponentsBuilder uriComponentsBuilder, Principal principal){

        CalendarDayViewModel calendarDayViewModel= calendarService.addNewEvent(calendarDayBindingModel, principal.getName());


        URI locationOfNewItem =
                URI.create(String.format("/api/stock/%s", calendarDayViewModel.getId()));

        return ResponseEntity.
                created(locationOfNewItem).
                body(calendarDayViewModel);




    }}
