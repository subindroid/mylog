package com.marumiru.mylog.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marumiru.mylog.dto.VenueDto;
import com.marumiru.mylog.service.VenueService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
public class RestVenueController {

    private final VenueService venueService;

    // GET /api/venues : 지도에 표시할 모든 회장 목록 반환
    @GetMapping
    public ResponseEntity<List<VenueDto>> getAllVenues() {
        List<VenueDto> venues = venueService.findAllVenues();
        return ResponseEntity.ok(venues);
    }

    // GET /api/venues/{id} : 회장 단건 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<VenueDto> getVenueById(@PathVariable Long id) {
        VenueDto venue = venueService.findVenueById(id);
        return ResponseEntity.ok(venue);
    }
}