package com.kim.reservation.domain.admin;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.kim.reservation.domain.goods.Goods;
import com.kim.reservation.domain.goods.GoodsRepository;
import com.kim.reservation.domain.reservation.ReservationRepository;
import com.kim.reservation.domain.reservation.Reservation.ReservationStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
	
	private final GoodsRepository goodsRepository;
	private final ReservationRepository reservationRepository;
	private final RedisTemplate<String, String> redisTemplate;
	
	public AdminMonitoringDto getMonitoringData(Long goodsId) {
		Goods goods = goodsRepository.findById(goodsId).orElseThrow();
		
		//1. 상태별 예약 수 조회
		long pending = reservationRepository.countByGoodsIdAndStatus(goodsId, ReservationStatus.PEADING);
		long confirmed= reservationRepository.countByGoodsIdAndStatus(goodsId, ReservationStatus.CONFIRMED);
		
		//2. Redis 대기열 인원 조회
		String queueKey = "waiting-queue:" + goodsId;
		Long waiting= redisTemplate.opsForZSet().zCard(queueKey);
		
		return new AdminMonitoringDto(
				goods.getName(), 
				goods.getStockQuantity(), 
				pending, 
				confirmed, 
				waiting != null ? waiting : 0
		);
		
	}

}
