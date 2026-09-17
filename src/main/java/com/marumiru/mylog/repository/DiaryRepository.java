package com.marumiru.mylog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marumiru.mylog.domain.Diary;

public interface DiaryRepository extends JpaRepository<Diary, Long>{

}
