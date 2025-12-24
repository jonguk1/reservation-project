package com.kim.reservation.domain.reservation;

import java.time.LocalDateTime;

import com.kim.reservation.domain.goods.Goods;
import com.kim.reservation.domain.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "reservation")
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
	
	public Reservation(User user, Goods goods) {
		this.user = user;
        this.goods = goods;
        this.reservationDate = LocalDateTime.now();
	}
	

}
