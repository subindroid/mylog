package com.marumiru.mylog.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marumiru.mylog.domain.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
