package com.marumiru.mylog.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.marumiru.mylog.domain.Event;
import com.marumiru.mylog.domain.User;
import com.marumiru.mylog.domain.Venue;
import com.marumiru.mylog.repository.DiaryRepository;
import com.marumiru.mylog.repository.EventRepository;
import com.marumiru.mylog.repository.UserRepository;
import com.marumiru.mylog.repository.VenueRepository;
import com.marumiru.mylog.service.AdminService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final VenueRepository venueRepository;
    private final EventRepository eventRepository;
    private final AdminService adminService;

    // 대시보드 메인 (4분할 데이터 렌더링)
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        PageRequest pageReq = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"));

        model.addAttribute("diaries", diaryRepository.findAll(pageReq));
        model.addAttribute("users", userRepository.findAll(pageReq));
        model.addAttribute("venues", venueRepository.findAll(pageReq));
        model.addAttribute("events", eventRepository.findAll(pageReq));

        return "admin/dashboard";
    }

    // [유저] 상태 변경 (Enable/Disable)
    @PostMapping("/users/toggle-enabled")
    public String toggleUserEnabled(@RequestParam Long userId, @AuthenticationPrincipal User currentUser) {
        adminService.toggleUserEnabled(userId, currentUser.getId());
        return "redirect:/admin/dashboard";
    }

    // [유저] Role 변경
    @PostMapping("/users/change-role")
    public String changeUserRole(@RequestParam Long userId,
                                 @RequestParam String roleName,
                                 @AuthenticationPrincipal User currentUser) {
        adminService.changeUserRole(userId, roleName, currentUser.getId());
        return "redirect:/admin/dashboard";
    }

    // [글] 삭제
    @PostMapping("/diaries/delete")
    public String deleteDiary(@RequestParam Long diaryId) {
        adminService.deleteDiary(diaryId);
        return "redirect:/admin/dashboard";
    }

    // [장소] 추가
    @PostMapping("/venues/add")
    public String addVenue(Venue venue) {
        adminService.saveVenue(venue);
        return "redirect:/admin/dashboard";
    }

    // [장소] 삭제
    @PostMapping("/venues/delete")
    public String deleteVenue(@RequestParam Long venueId) {
        adminService.deleteVenue(venueId);
        return "redirect:/admin/dashboard";
    }

    // [이벤트] 추가
    @PostMapping("/events/add")
    public String addEvent(Event event) {
        adminService.saveEvent(event);
        return "redirect:/admin/dashboard";
    }

    // [이벤트] 삭제
    @PostMapping("/events/delete")
    public String deleteEvent(@RequestParam Long eventId) {
        adminService.deleteEvent(eventId);
        return "redirect:/admin/dashboard";
    }
}