package com.marumiru.mylog.domain;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false)
    private Long id; // pk

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp 
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id") // DB users 테이블의 foreign key 컬럼명
    private Role role;

    @Builder // 생성자. BoardUser를 Builder 패턴으로 작성
    public User(String username, String password, LocalDateTime createdAt, Role role) {
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
        if (role != null) {
            this.role = role;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 단일 Role 객체에서 권한 이름을 가져와 리스트로 반환
        if (this.role == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(new SimpleGrantedAuthority(this.role.getRolename()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    // 무조건 true. 따로 로직 구현 X
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    // 무조건 true. 따로 로직 구현 X
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    // 무조건 true. 따로 로직 구현 X
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    // 무조건 true. 따로 로직 구현 X
    public boolean isEnabled() {
        return true;
    }
}
