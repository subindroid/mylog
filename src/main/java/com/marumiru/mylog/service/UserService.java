package com.marumiru.mylog.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marumiru.mylog.domain.Role;
import com.marumiru.mylog.domain.User;
import com.marumiru.mylog.dto.AddUserRequest;
import com.marumiru.mylog.repository.RoleRepository;
import com.marumiru.mylog.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    @Transactional
    public Long save(AddUserRequest dto) {
        // 1. DTO에 role 값이 없으면 "ROLE_USER"를 기본값으로 사용
        String targetRoleName = (dto.getRole() != null && !dto.getRole().isBlank())
                ? dto.getRole()
                : "ROLE_USER";

        // "ADMIN"이나 "USER" 형태로 들어올 경우 "ROLE_" 접두사 보장
        if (!targetRoleName.startsWith("ROLE_")) {
            targetRoleName = "ROLE_" + targetRoleName;
        }

        Role userRole = roleRepository.findByRolename(targetRoleName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 권한입니다."));

        return userRepository.save(User.builder()
                .username(dto.getUsername())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .role(userRole) // 단일 Role 세팅
                .build()).getId();
    }

    public User getUser(String username) {
        Optional<User> _user = userRepository.findByUsername(username);
        if (_user.isPresent()) {
            return _user.get();
        } else {
            throw new UserNotFoundException("boardUser not found");
        }
    }
}
