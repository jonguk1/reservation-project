package com.kim.reservation.global.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kim.reservation.domain.goods.Goods;
import com.kim.reservation.domain.goods.GoodsRepository;

@Configuration
public class DataInitConfig {

	
	@Bean
	public CommandLineRunner initData(GoodsRepository goodsRepository) {
		return args->{
			// 서버 시작 시 샘플 상품 2개 저장
            goodsRepository.save(new Goods("2025 임영웅 콘서트 티켓", 150000, 100));
            goodsRepository.save(new Goods("아이폰 16 프로 예약", 1700000, 10));
            
            System.out.println(">> 샘플 상품 데이터가 생성되었습니다.");
		};
	}
}
