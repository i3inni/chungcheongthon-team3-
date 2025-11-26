package com.hackathon.knut.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Spring Security가 활성화되어 있으므로 SecurityConfig에서 CORS를 처리합니다.
// 이 설정은 SecurityConfig의 CORS 설정보다 우선순위가 낮아서 실제로는 적용되지 않습니다.
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // CORS 설정을 SecurityConfig로 이동했으므로 주석 처리
    /*
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                    "http://localhost:5173",  // Vite 개발 서버
                    "http://localhost:3000",  // React 개발 서버 (예비)
                    "http://127.0.0.1:5173"   // 로컬호스트 대안
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
    */
}
