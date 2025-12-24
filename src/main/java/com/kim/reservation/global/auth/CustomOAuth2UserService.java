
package com.kim.reservation.global.auth;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;


import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;

import com.kim.reservation.domain.user.User;
import com.kim.reservation.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	@Override public OAuth2User loadUser(OAuth2UserRequest userRequest) throws 
	OAuth2AuthenticationException{ 
		OAuth2User oAuth2User = super.loadUser(userRequest);
  
		//4. 우리 서비스에 필요한 유저 정보 추출 
		String email = oAuth2User.getAttribute("email");
		String name = oAuth2User.getAttribute("name");
  
		// 5. DB 저장 혹은 업데이트 로직 (가입/로그인 처리) 
		saveOrUpdate(email, name);
  
		// 6. 스프링 시큐리티 내부에서 사용할 유저 객체 반환 
		return oAuth2User; }

	private User saveOrUpdate(String email, String name) {
		User user = userRepository.findByEmail(email).map(entity -> entity.update(name))
				.orElse(User.builder().email(email).name(name).role("ROLE_USER").build());
		return userRepository.save(user);
	}

}
