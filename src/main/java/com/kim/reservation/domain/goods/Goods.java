package com.kim.reservation.domain.goods;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "goods")
@AllArgsConstructor
@Builder
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
	
	@Column
	private LocalDateTime openTime;
	
	
	//생성자
	public Goods(String name, Integer price, Integer stockQuantity,LocalDateTime openTime) {
		this.name=name;
		this.price=price;
		this.stockQuantity=stockQuantity;
		this.openTime=openTime;
	}
	
	// 재고 줄이기
	public void removeStock(int quantity) {
		int restStock = this.stockQuantity - quantity;
		if(restStock < 0) {
			throw new RuntimeException("재고가 부족합니다");
		}
		this.stockQuantity = restStock;
	}
	
	//재고 복구 
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }
	
    public void setStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("재고는 0개 이상이어야 합니다.");
        }
        this.stockQuantity = stockQuantity;
    }
	

}
