package com.marumiru.mylog.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marumiru.mylog.domain.Profile;
import com.marumiru.mylog.domain.User;
import com.marumiru.mylog.repository.ProfileRepository;
import com.marumiru.mylog.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public Optional<Profile> viewProfile(Long userId) {
        return profileRepository.findById(userId);
    }

    @Transactional
    public void addProfile(Long userId, String nickname, String bio, String location) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. id=" + userId));

        Profile profile = Profile.builder()
                .nickname(nickname)
                .bio(bio)
                .location(location)
                .user(user)
                .build();

        profileRepository.save(profile);
    }

    // 변경 감지(Dirty Checking) 적용
    @Transactional
    public void editProfile(Long userId, String nickname, String bio, String location) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로필입니다. userId=" + userId));

        profile.setNickname(nickname);
        profile.setBio(bio);
        profile.setLocation(location);
        // @Transactional에 의해 메서드 종료 시 자동 UPDATE 쿼리 실행
    }
}