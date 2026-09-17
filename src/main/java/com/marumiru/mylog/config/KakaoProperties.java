package com.marumiru.mylog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "kakao.api")
@Getter
@Setter
public class KakaoProperties {
    private String key; // application.properties의 kakao.api.key와 자동 매핑
}