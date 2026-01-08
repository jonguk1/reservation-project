package com.kim.reservation.domain.reservation;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.kim.reservation.domain.goods.Goods;
import com.kim.reservation.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "reservation")
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Reservation {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "goods_id")
	private Goods goods;
	
	private LocalDateTime reservationDate;
	
	@Enumerated(EnumType.STRING)
	private ReservationStatus status;
	
	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt;
	
	public Reservation(User user, Goods goods) {
		this.user = user;
        this.goods = goods;
        this.reservationDate = LocalDateTime.now();
	}
	
	public enum ReservationStatus{
		PEADING,   // 재고 선점(결제 대기중)
		CONFIRMED, // 결제 완료
		CANCELLED, //취소
		EXPIRED    //점유 시간 만료
	}
	
	// 상태 변경 메서드
    public void confirm() {
        this.status = ReservationStatus.CONFIRMED;
        this.reservationDate = LocalDateTime.now(); // 확정되는 순간의 시간을 기록
    }
	

}
