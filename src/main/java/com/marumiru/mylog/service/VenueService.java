package com.marumiru.mylog.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marumiru.mylog.domain.Venue;
import com.marumiru.mylog.dto.VenueDto;
import com.marumiru.mylog.repository.VenueRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VenueService {

    private final VenueRepository venueRepository;

    // 1. 등록된 모든 회장 리스트 조회 (지도 마커 렌더링용)
    public List<VenueDto> findAllVenues() {
        return venueRepository.findAll().stream()
                .map(VenueDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 2. 단건 회장 상세 조회
    public VenueDto findVenueById(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회장입니다. id=" + id));
        return VenueDto.fromEntity(venue);
    }

}