package com.marumiru.mylog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marumiru.mylog.domain.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

}
