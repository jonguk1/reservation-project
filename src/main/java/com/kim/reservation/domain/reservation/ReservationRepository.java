package com.kim.reservation.domain.reservation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kim.reservation.domain.reservation.Reservation.ReservationStatus;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{
	
	//특정 유저 ID로 예약내역 보기
	List<Reservation> findByUserId(Long userId);
	
	//상태가 특정값이고, 생성 시간이 특정 시간보다 이전인 데이터 찾기
	List<Reservation> findAllByStatusAndCreatedAtBefore(ReservationStatus status, LocalDateTime dateTime);

	//상태별 예약 수 조회
	long countByGoodsIdAndStatus(Long goodsId, ReservationStatus status);
	
	//나의 예약 조회
	List<Reservation> findByUserIdOrderByReservationDateDesc(Long userId);
}
