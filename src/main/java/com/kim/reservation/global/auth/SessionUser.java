package com.kim.reservation.global.auth;

import com.kim.reservation.domain.user.User;

import lombok.Getter;

@Getter
public class SessionUser {
	private Long id;
	private String name;
	private String email;
	
	public SessionUser(User user) {
		this.id = user.getId();
		this.name=user.getName();
		this.email=user.getEmail();
	}

}
