package com.kim.reservation.domain.reservation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReservationController {
	
	private final ReservationService reservationService;
	
	// 테스트용 예약 주소: http://localhost:8080/reserve?userId=1&goodsId=1
	@GetMapping("/reserve")
	public String reserve(@RequestParam Long userId,@RequestParam Long goodsId) {
		try {
			Long reservationId= reservationService.reserve(userId, goodsId);
			return "예약 성공! 예약 번호: " + reservationId;
		}catch (Exception e) {
			return "예약 실패: " + e.getMessage();
		}
		
	}

}
