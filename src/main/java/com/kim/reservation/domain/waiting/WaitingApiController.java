package com.kim.reservation.domain.waiting;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kim.reservation.global.auth.SessionUser;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/waiting")
@RequiredArgsConstructor
public class WaitingApiController {
	
	private final WaitingQueueService waitingQueueService;
	
	@PostMapping("/join")
	public ResponseEntity<Long> joinQuene(@RequestParam Long goodsId, HttpSession session){
		
		SessionUser user = (SessionUser) session.getAttribute("user");
		if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		// 대기열에 등록하고 내 순번 반환
		Long rank= waitingQueueService.registerQueue(goodsId, user.getId().toString());
		return ResponseEntity.ok(rank);
	}

}
