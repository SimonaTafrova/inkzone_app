package com.example.inkzone.repository;

import com.example.inkzone.model.entity.CalendarDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CalendarRepository extends JpaRepository<CalendarDay, Long> {

    Optional<CalendarDay> findByDayAndMonthAndYear(Integer day, Integer month, Integer year);


}
