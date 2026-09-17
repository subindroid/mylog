package com.marumiru.mylog.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marumiru.mylog.dto.EventDto;
import com.marumiru.mylog.repository.EventRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class RestEventController {

    private final EventRepository eventRepository;

    @GetMapping
    public ResponseEntity<List<EventDto>> getEventsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<EventDto> events = eventRepository.findByEventDateBetween(startOfDay, endOfDay)
                .stream()
                .map(EventDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(events);
    }
}