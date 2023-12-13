package com.example.inkzone.repository;

import com.example.inkzone.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event,Long> {

    Optional<List<Event>> getAllByCreator_Email(String email);




}
