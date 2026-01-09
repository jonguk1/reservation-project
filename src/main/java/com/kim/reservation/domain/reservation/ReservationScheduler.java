package com.kim.reservation.domain.reservation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kim.reservation.domain.goods.Goods;
import com.kim.reservation.domain.goods.GoodsRepository;
import com.kim.reservation.domain.reservation.Reservation.ReservationStatus;
import com.kim.reservation.domain.waiting.WaitingQueueService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationScheduler {
	
	private final ReservationRepository reservationRepository;
	private final WaitingQueueService waitingQueueService;
	
	//1분마다 실행
	@Scheduled(fixedDelay = 60000)
	@Transactional
	public void cleanupExpiredReservations() {
		
		//LocalDateTime threshold = LocalDateTime.now().minusMinutes(5);
		LocalDateTime threshold = LocalDateTime.now().minusMinutes(1);
		
		//1. 5분 전보다 일찍 생성되었고, 아직 PENDING 상태인 예약 조회
		List<Reservation> expiredReservations = reservationRepository
				.findAllByStatusAndCreatedAtBefore(ReservationStatus.PENDING, threshold);
		
		//없다면 무시
		if (expiredReservations.isEmpty()) return;
		
		log.info("만료된 예약 {}건을 처리합니다", expiredReservations.size());
		
		for(Reservation reservation: expiredReservations) {
			//2. 상태 변경
			reservation.setStatus(ReservationStatus.EXPIRED);
			
			//재고 복구
			Goods goods = reservation.getGoods();
			goods.addStock(1);
			
			log.info("예약 ID {} 만료 처리 -> 상품 {} 재고 1개 복구", 
                    reservation.getId(), goods.getName());
			
			waitingQueueService.removeActiveUser(reservation.getGoods().getId(), 
					reservation.getUser().getId().toString());
		}
	}
	
	

}
