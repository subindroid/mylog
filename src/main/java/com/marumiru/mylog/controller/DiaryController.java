package com.marumiru.mylog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.marumiru.mylog.domain.Diary;
import com.marumiru.mylog.domain.Event;
import com.marumiru.mylog.domain.User;
import com.marumiru.mylog.domain.Venue;
import com.marumiru.mylog.repository.EventRepository;
import com.marumiru.mylog.repository.VenueRepository;
import com.marumiru.mylog.service.DiaryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;
    private final VenueRepository venueRepository;
    private final EventRepository eventRepository;

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    @GetMapping({ "/listDiary", "/" })
    public String getDiaryList(Model model) {
        List<Diary> diaries = diaryService.listDiaries();
        model.addAttribute("dataList", diaries);
        return "listDiary";
    }

    @GetMapping("/viewDiary")
    public ModelAndView viewDiary(@RequestParam Long no) {
        Diary diary = diaryService.viewDiary(no);
        ModelAndView mv = new ModelAndView("viewDiary");
        mv.addObject("diary", diary);
        return mv;
    }

    // 글쓰기 폼 페이지 이동
    @GetMapping("/add")
    public String writeDiaryForm(Model model) {
        model.addAttribute("kakaoApiKey", kakaoApiKey);
        return "writeDiary"; // writeDiary.html 리턴
    }

// 글쓰기 저장 처리 (POST)
@PostMapping("/add")
public String addDiary(@RequestParam String title,
                       @RequestParam String content,
                       @RequestParam(required = false) Long venueId,
                       @RequestParam(required = false) String customVenue, // 직접 입력된 공연장 이름
                       @AuthenticationPrincipal User currentUser,
                       @RequestParam(required = false) Long eventId) {
    if (currentUser == null) {
        return "redirect:/diary/login";
    }

    Diary diary = new Diary();
    diary.setTitle(title);
    diary.setContent(content);
    diary.setUser(currentUser);

    // 1. 지도 마커로 공식 회장을 선택한 경우 (venue_id FK 저장)
    if (venueId != null) {
        Venue venue = venueRepository.findById(venueId).orElse(null);
        diary.setVenue(venue); // ★ 주석 해제 및 엔티티 세팅
        diary.setCustomVenue(null); // 지도 마커 선택 시 직접입력 값은 null 처리
    } 
    // 2. 지도 마커에 없어서 직접 입력한 경우 (venue_id는 null, customVenue에만 텍스트 저장)
    else if (customVenue != null && !customVenue.trim().isEmpty()) {
        diary.setVenue(null); // ★ venue_id는 NULL로 명시적 처리
        diary.setCustomVenue(customVenue.trim()); // ★ 직접 입력한 텍스트 세팅
    }

    // 3. 라이브 이벤트를 선택한 경우 (Event 엔티티 연관관계 매핑)
    if (eventId != null) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event != null) {
            diary.setEvent(event);
        }
    }

    diaryService.addDiary(diary);
    return "redirect:/diary/listDiary";
}

    // 수정 처리 (본인 검증)
    @PostMapping("/edit")
    public String editDiary(@RequestParam Long id,
            @RequestParam String title,
            @RequestParam String content,
            @AuthenticationPrincipal User currentUser,
            RedirectAttributes redirectAttr) {
        if (currentUser == null) {
            return "redirect:/diary/login";
        }

        Diary diary = diaryService.viewDiary(id);

        // 본인 작성글인지 2차 보안 검증 (User PK 비교)
        if (diary.getUser() == null || !diary.getUser().getId().equals(currentUser.getId())) {
            return "redirect:/diary/listDiary";
        }

        diaryService.editDiary(id, title, content);
        redirectAttr.addAttribute("no", id);
        return "redirect:/diary/viewDiary";
    }

    // DiaryController.java 내부 removeDiary & editDiary 메서드 수정 예시

    @PostMapping("/remove")
    public String removeDiary(@RequestParam(value = "diaryNo", required = false) Long diaryNo,
            @RequestParam(value = "id", required = false) Long id,
            @AuthenticationPrincipal User currentUser) {

        if (currentUser == null) {
            return "redirect:/diary/login";
        }

        Long targetId = (diaryNo != null) ? diaryNo : id;
        if (targetId == null) {
            return "redirect:/diary/listDiary";
        }

        Diary diary = diaryService.viewDiary(targetId);

        // 1. 관리자 권한 보유 여부 확인 (ROLE_ADMIN 체크)
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        // 2. 작성자 본인 확인
        boolean isAuthor = diary.getUser() != null && diary.getUser().getId().equals(currentUser.getId());

        // 3. 본인도 아니고 관리자도 아니면 삭제 금지
        if (!isAuthor && !isAdmin) {
            return "redirect:/diary/listDiary";
        }

        diaryService.removeDiary(targetId);
        return "redirect:/diary/listDiary";
    }
}