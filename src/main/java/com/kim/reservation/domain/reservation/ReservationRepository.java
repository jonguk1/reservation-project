package com.kim.reservation.domain.reservation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{
	
	//특정 유저 ID로 예약내역 보기
	List<Reservation> findByUserId(Long userId);

}
