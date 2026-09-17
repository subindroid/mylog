package com.marumiru.mylog.controller;

import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.marumiru.mylog.domain.Profile;
import com.marumiru.mylog.domain.User;
import com.marumiru.mylog.service.ProfileService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/diary/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public String viewProfile(Model model, @AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            return "redirect:/diary/login";
        }

        Optional<Profile> profileOptional = profileService.viewProfile(currentUser.getId());
        profileOptional.ifPresent(profile -> model.addAttribute("profile", profile));

        return "profile";
    }

    @GetMapping("/add")
    public String writeProfile() {
        return "writeProfile";
    }

    @PostMapping("/addprofile")
    public String addProfile(@RequestParam String nickname,
            @RequestParam String bio,
            @RequestParam String location,
            @AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            return "redirect:/diary/login";
        }

        profileService.addProfile(currentUser.getId(), nickname, bio, location);

        return "redirect:/diary/profile";
    }

    // 수정 처리 (본인 검증 및 경로 수정)
    @PostMapping("/edit")
    public String editProfile(@RequestParam String nickname,
            @RequestParam String bio,
            @RequestParam String location,
            @AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            return "redirect:/diary/login";
        }

        // 1. DB에서 현재 사용자의 프로필이 존재하는지 조회
        Optional<Profile> profileOptional = profileService.viewProfile(currentUser.getId());
        if (profileOptional.isEmpty()) {
            return "redirect:/diary/profile";
        }

        // 2. 서비스 호출하여 변경 감지(Dirty Checking)로 데이터 수정
        profileService.editProfile(currentUser.getId(), nickname, bio, location);

        // 3. 올바른 프로필 조회 경로로 리다이렉트
        return "redirect:/diary/profile";
    }
}