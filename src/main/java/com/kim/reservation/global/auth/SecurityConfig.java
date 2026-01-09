package com.kim.reservation.global.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. CSRF 비활성화 (H2 콘솔 접속 및 API 테스트를 위해)
            .csrf(csrf -> csrf.disable())

            // 2. H2 콘솔 프레임 노출 허용 (동일 출처에서만)
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
            )

            // 3. URL별 권한 설정
            .authorizeHttpRequests(auth -> auth
                // H2 콘솔 및 기본 페이지들을 인증 없이 허용
                .requestMatchers("/", "/h2-console/**", "/test-login").permitAll()
                // 관리자 대시보드
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // 그 외 요청은 로그인 필요
                .anyRequest().authenticated()
            )

            // 4. 로그아웃 설정
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true) // 세션 무효화
                .clearAuthentication(true)   // 인증 정보 삭제
                .deleteCookies("JSESSIONID") // 쿠키 삭제
                .permitAll()
            )

            // 5. OAuth2 로그인 설정
            .oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("/goods", true) // 로그인 성공 시 이동할 주소
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService)
                )
            );

        return http.build();
    }
}