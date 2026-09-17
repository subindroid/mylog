package com.marumiru.mylog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @PreAuthorize("hasRole('ROLE_USER')") // 메서드 단위 권한 제어
    @PostMapping("/order")
    public ResponseEntity<String> createOrder() {
        return ResponseEntity.ok("주문 성공");
    }
}

