package com.marumiru.mylog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "user_profile")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Profile {
    @Id
    private Long id; // @GeneratedValue를 붙이지 않음 (User의 ID를 가져와서 사용)

    @Column(length = 50)
    private String nickname;

    @Column(length = 255)
    private String bio;

    @Column 
    private String location;

    @MapsId // Profile.id 필드에 User의 PK(user_id)를 그대로 매핑시킴
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // DB 상의 FK 컬럼명이자 PK 컬럼명이 됨
    private User user;

    @Builder
    public Profile(String nickname, String bio, User user, String location) {
        this.nickname = nickname;
        this.bio = bio;
        this.user = user;
        this.location = location;
    }
}