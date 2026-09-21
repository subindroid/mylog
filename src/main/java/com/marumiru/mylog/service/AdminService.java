package com.marumiru.mylog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marumiru.mylog.domain.Event;
import com.marumiru.mylog.domain.Role;
import com.marumiru.mylog.domain.User;
import com.marumiru.mylog.domain.Venue;
import com.marumiru.mylog.repository.DiaryRepository;
import com.marumiru.mylog.repository.EventRepository;
import com.marumiru.mylog.repository.RoleRepository;
import com.marumiru.mylog.repository.UserRepository;
import com.marumiru.mylog.repository.VenueRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final DiaryRepository diaryRepository;

    // 1. 유저 Enable/Disable 토글 (안전장치 적용)
    public void toggleUserEnabled(Long targetUserId, Long currentUserId) {
        // [안전장치] 본인 계정 제어 방지
        if (targetUserId.equals(currentUserId)) {
            throw new IllegalArgumentException("자기 자신의 계정은 비활성화할 수 없습니다.");
        }

        User user = userRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. id=" + targetUserId));

        user.changeEnabled(!user.isEnabled());
    }

    // AdminService.java 내부 changeUserRole 메서드 수정 예시

    @Transactional
    public void changeUserRole(Long targetUserId, String newRoleName, Long currentUserId) {
        // 1. [안전장치] 본인 권한 변경(다운그레이드 포함) 방지
        if (targetUserId.equals(currentUserId)) {
            throw new IllegalArgumentException("자기 자신의 권한은 변경할 수 없습니다.");
        }

        User user = userRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. id=" + targetUserId));

        // "ADMIN"이나 "USER"로 들어왔을 때 "ROLE_" 접두사 보장
        String targetRoleName = newRoleName.startsWith("ROLE_") ? newRoleName : "ROLE_" + newRoleName;

        // 2. DB에서 변경할 단일 Role 엔티티 조회[cite: 1]
        Role role = roleRepository.findByRolename(targetRoleName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 권한입니다: " + targetRoleName));

        // 3. 단일 Role 업데이트 메서드 호출
        user.updateRole(role);
    }

    // 3. 다이어리 삭제 (관리자 권한)
    public void deleteDiary(Long diaryId) {
        diaryRepository.deleteById(diaryId);
    }

    // 4. Venue (공연장) CUD
    public void saveVenue(Venue venue) {
        venueRepository.save(venue);
    }

    public void deleteVenue(Long venueId) {
        venueRepository.deleteById(venueId);
    }

    // 5. Event (라이브 이벤트) CUD
    public void saveEvent(Event event) {
        eventRepository.save(event);
    }

    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }
}