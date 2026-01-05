
package com.kim.reservation.global.auth;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;

import org.hibernate.internal.util.beans.BeanInfoHelper.ReturningBeanInfoDelegate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;

import com.kim.reservation.domain.user.User;
import com.kim.reservation.domain.user.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;
	private final HttpSession httpSession;

	@Override public OAuth2User loadUser(OAuth2UserRequest userRequest) throws 
	OAuth2AuthenticationException{ 
		OAuth2User oAuth2User = super.loadUser(userRequest);
  
		//1. 구글로부터 유저 정보 추출 
		String email = oAuth2User.getAttribute("email");
		String name = oAuth2User.getAttribute("name");
  
	    //2. DB 저장 혹은 업데이트
		User user = saveOrUpdate(email, name);
		
		//3 세션에 Session(DTO) 저장
		httpSession.setAttribute("user", new SessionUser(user));
		
		// 4. 시큐리티에서 사용할 권한과 정보를 포함한 객체 반환
		return new DefaultOAuth2User(
				Collections.singleton(new SimpleGrantedAuthority(user.getRole())), 
				oAuth2User.getAttributes(), 
				"email");
	}
		

	private User saveOrUpdate(String email, String name) {
		User user = userRepository.findByEmail(email)
				.map(entity -> entity.update(name))
				.orElse(User.builder()
						.email(email)
						.name(name)
						.role("ROLE_USER")
						.build());
		
		return userRepository.save(user);
	}

}
