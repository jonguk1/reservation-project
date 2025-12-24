package com.kim.reservation.domain.goods;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "goods")
public class Goods {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private Integer price;
	
	@Column(nullable = false)
	private Integer stockQuantity;
	
	public Goods(String name, Integer price, Integer stockQuantity) {
		this.name=name;
		this.price=price;
		this.stockQuantity=stockQuantity;
	}
	
	// 재고를 줄이는 메서드
	public void removeStock(int quantity) {
		int restStock = this.stockQuantity - quantity;
		if(restStock<0) {
			throw new RuntimeException("재고가 부족합니다");
		}
		this.stockQuantity = restStock;
	}
	
	
	

}
