package com.marumiru.mylog.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marumiru.mylog.domain.Diary;
import com.marumiru.mylog.repository.DiaryRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
@Transactional(readOnly = true)
public class DiaryService {

    private final DiaryRepository diaryRepository;

    public List<Diary> listDiaries() {
        return diaryRepository.findAll(Sort.by(Sort.Direction.DESC, Diary::getId));
    }

    @Transactional
    public void addDiary(Diary diary) {
        diaryRepository.save(diary);
    }

    public Diary viewDiary(Long no) {
        return diaryRepository.findById(no)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다. id=" + no));
    }

    // Dirty Checking(변경 감지) 적용
    @Transactional
    public void editDiary(Long id, String title, String content) {
        Diary diary = viewDiary(id);
        diary.setTitle(title);
        diary.setContent(content);
        // @Transactional에 의해 메서드 종료 시 자동 UPDATE 쿼리 실행
    }

    @Transactional
    public void removeDiary(Long diaryNo) {
        diaryRepository.deleteById(diaryNo);
    }
}