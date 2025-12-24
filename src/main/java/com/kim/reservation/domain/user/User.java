package com.kim.reservation.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 100, unique = true)
	private String email;
	
	@Column(nullable = false, length = 50)
	private String name;
	
	@Column(nullable = false)
	private String role;
	
	@Builder
	public User(String email,String name,String role) {
		this.email = email;
		this.name = name;
		this.role = role;
	}
	
	// 소셜 정보가 변경될 경우를 대비한 업데이트 메서드
    public User update(String name) {
        this.name = name;
        return this;
    }

}
