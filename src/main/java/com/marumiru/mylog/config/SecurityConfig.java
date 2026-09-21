package com.marumiru.mylog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.marumiru.mylog.service.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    private static final String[] PERMIT_ALL_PATTERNS = {
            "/diary/login",
            "/diary/signup",
            "/diary/user"
    };

    @Bean
    WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                // 정적 파일만 시큐리티 필터 예외 처리
                .requestMatchers("/static/**", "/css/**", "/js/**", "/images/**");
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        // 1. 로그인/가입 관련 누구나 접근 가능
                        .requestMatchers(PERMIT_ALL_PATTERNS).permitAll()

                        // 2. 관리자 전용
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 3. 다이어리 관련 모든 서비스(/diary/**)는 로그인된 사용자(USER, ADMIN) 허용
                        .requestMatchers("/diary/**").hasAnyRole("USER", "ADMIN")

                        // 4. 지도 회장 API (/api/venues/**) - 맨 앞에 / 추가하여 정상 허용
                        .requestMatchers("/api/venues/**").hasAnyRole("USER", "ADMIN")

                        // 5. 나머지 모든 요청 인증 필요
                        .anyRequest().authenticated())

                .formLogin(frm -> frm
                        .loginPage("/diary/login")
                        .loginProcessingUrl("/diary/login") // POST 로그인 처리 URL 명시
                        .defaultSuccessUrl("/diary/listDiary", true))

                .logout(logout -> logout
                        .logoutUrl("/diary/logout")
                        .logoutSuccessUrl("/diary/login")
                        .invalidateHttpSession(true))
                .build();
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
            UserDetailsService userDetailsService) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);

        return new ProviderManager(authProvider);
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}