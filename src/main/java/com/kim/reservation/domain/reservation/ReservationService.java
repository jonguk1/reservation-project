package com.kim.reservation.domain.reservation;

import org.springframework.stereotype.Service;

import com.kim.reservation.domain.goods.Goods;
import com.kim.reservation.domain.goods.GoodsRepository;
import com.kim.reservation.domain.reservation.Reservation.ReservationStatus;
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
				.orElseThrow(()->new IllegalArgumentException("사용자를 찾을 수 없습니다."));
		Goods goods = goodsRepository.findById(goodsId)
				.orElseThrow(()->new IllegalArgumentException("상품을 찾을 수 없습니다."));
		
		//2. 재고 감소
		goods.removeStock(1);
		
		//3. 예약 생성 및 저장
		Reservation reservation = Reservation.builder()
				.user(user)
				.goods(goods)
				.status(ReservationStatus.PEADING)
				.build();
		
		reservationRepository.save(reservation);
		
		return reservation.getId();
		
	}
	
	//확정처리
	@Transactional
	public void confirmReservation(Long reservationId) {
		Reservation reservation = reservationRepository.findById(reservationId)
				.orElseThrow(()->new IllegalArgumentException("예약 내역을 찾을 수 없습니다."));
		
		//이미 취소되었거나 만료된 예약인 경우
		if(reservation.getStatus() != ReservationStatus.PEADING) {
			throw new IllegalArgumentException("결제 가능한 상태가 아닙니다.");
		}
		
		//상태를 CONFIRMED로 변경
		reservation.confirm();
	}
	
	

}
