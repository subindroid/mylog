package com.marumiru.mylog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marumiru.mylog.domain.Venue;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    // 이미 등록된 장소인지 이름과 주소로 찾아보기 (중복 저장 방지용)
    Optional<Venue> findByNameAndAddress(String name, String address);
}
