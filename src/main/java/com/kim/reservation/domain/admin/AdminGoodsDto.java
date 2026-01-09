package com.kim.reservation.domain.admin;

import com.kim.reservation.domain.goods.Goods;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminGoodsDto {
	private Goods goods;      // 상품 엔티티 정보
    private long waitCount;   // 현재 Redis 대기열 인원
    private long activeCount; // 현재 결제 진행 중인 인원
    
    //생성자
    public AdminGoodsDto(Goods goods, long waitCount, long activeCount) {
    	this.goods=goods;
    	this.waitCount = waitCount;
        this.activeCount = activeCount;
    }

}
