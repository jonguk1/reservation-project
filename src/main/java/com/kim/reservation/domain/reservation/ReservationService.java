package com.kim.reservation.domain.reservation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kim.reservation.domain.goods.Goods;
import com.kim.reservation.domain.goods.GoodsRepository;
import com.kim.reservation.domain.reservation.Reservation.ReservationStatus;
import com.kim.reservation.domain.user.User;
import com.kim.reservation.domain.user.UserRepository;
import com.kim.reservation.domain.waiting.WaitingQueueService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReservationService {
	
	private final ReservationRepository reservationRepository;
	private final GoodsRepository goodsRepository;
	private final UserRepository userRepository;
	private final WaitingQueueService waitingQueueService;
	
	/**
	 * 예약 처리
	 */
	@Transactional
	public Long reserve(Long userId, Long goodsId) {
		//1. 엔티티 조회
		User user = userRepository.findById(userId)
				.orElseThrow(()->new IllegalArgumentException("사용자를 찾을 수 없습니다."));
		Goods goods = goodsRepository.findById(goodsId)
				.orElseThrow(()->new IllegalArgumentException("상품을 찾을 수 없습니다."));
		
		// 재고가 이미 0이라면 즉시 대기열을 터트리고 예외 발생
		if (goods.getStockQuantity() <= 0) {
	        waitingQueueService.processSoldOut(goodsId);
	        throw new RuntimeException("이미 품절된 상품입니다.");
	    }
		
		//2. 재고 감소
		goods.removeStock(1);
		
		//재고를 깎았는데 딱 0이 되었다면 대기열 청소
	    if (goods.getStockQuantity() == 0) {
	        waitingQueueService.processSoldOut(goodsId);
	        log.info("ddddd");
	    }
		
		//3. 예약 생성 및 저장
		Reservation reservation = Reservation.builder()
				.user(user)
				.goods(goods)
				.status(ReservationStatus.PENDING)
				.build();
		
		reservationRepository.save(reservation);
		
		return reservation.getId();
		
	}
	
	/**
	 * 예약 확정 처리
	 */
	@Transactional
	public void confirmReservation(Long reservationId) {
		Reservation reservation = reservationRepository.findById(reservationId)
				.orElseThrow(()->new IllegalArgumentException("예약 내역을 찾을 수 없습니다."));
		
		//이미 취소되었거나 만료된 예약인 경우
		if(reservation.getStatus() != ReservationStatus.PENDING) {
			throw new IllegalArgumentException("결제 가능한 상태가 아닙니다.");
		}
		
		//상태를 CONFIRMED로 변경
		reservation.confirm();
		
		//결제 성공했으니 활성 유저 명단에서 삭제하여 다음 사람 자리를 만들어줌
		waitingQueueService.removeActiveUser(reservation.getGoods().getId(), 
				reservation.getUser().getId().toString());
		
	}
	
	/**
	 * 내 예약 조회
	 */
	public List<Reservation> findMyReservations(Long userId){
		return reservationRepository.findByUserIdOrderByReservationDateDesc(userId);
	}
	
	/**
	 * 예약 ID로 예약 단건 조회
	 */
	public Reservation findById(Long reservationId) {
	    return reservationRepository.findById(reservationId)
	            .orElseThrow(() -> new IllegalArgumentException("해당 예약 내역을 찾을 수 없습니다. ID: " + reservationId));
	}
	
	

}
