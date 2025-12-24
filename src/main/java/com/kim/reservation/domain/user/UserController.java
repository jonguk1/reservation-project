package com.kim.reservation.domain.user;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/test-login")
    public Map<String, Object> testLogin(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return Map.of("error", "로그인 세션이 없습니다.");
        }
        return principal.getAttributes();
    }
}
