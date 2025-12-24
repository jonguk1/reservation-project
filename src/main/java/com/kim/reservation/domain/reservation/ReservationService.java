package com.kim.reservation.domain.reservation;

import org.springframework.stereotype.Service;

import com.kim.reservation.domain.goods.Goods;
import com.kim.reservation.domain.goods.GoodsRepository;
import com.kim.reservation.domain.user.User;
import com.kim.reservation.domain.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {
	
	private final ReservationRepository reservationRepository;
	private final GoodsRepository goodsRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public Long reserve(Long userId, Long goodsId) {
		//1. 엔티티 조회
		User user = userRepository.findById(userId)
				.orElseThrow(()->new IllegalArgumentException("사용자를 찾을 수 없습니다"));
		Goods goods = goodsRepository.findById(goodsId)
				.orElseThrow(()->new IllegalArgumentException("상품을 찾을 수 없습니다"));
		
		//2. 재고 감소
		goods.removeStock(1);
		
		//3. 예약 생성 및 저장
		Reservation reservation = new Reservation(user,goods);
		reservationRepository.save(reservation);
		
		return reservation.getId();
		
	}

}
