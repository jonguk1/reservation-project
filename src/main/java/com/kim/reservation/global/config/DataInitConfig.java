package com.kim.reservation.global.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kim.reservation.domain.goods.Goods;
import com.kim.reservation.domain.goods.GoodsRepository;

import java.time.LocalDateTime; // 추가

@Configuration
public class DataInitConfig {

    @Bean
    public CommandLineRunner initData(GoodsRepository goodsRepository) {
        return args -> {
            // 1. 임영웅 티켓: 지금으로부터 1분 뒤에 예약 시작 (타이머 테스트용)
            goodsRepository.save(Goods.builder()
                    .name("2025 임영웅 콘서트 티켓")
                    .price(150000)
                    .stockQuantity(100)
                    .openTime(LocalDateTime.now().plusSeconds(5)) 
                    .build());

            // 2. 아이폰: 지금 바로 예약 가능 (이미 시간이 지남)
            goodsRepository.save(Goods.builder()
                    .name("아이폰 16 프로 예약")
                    .price(1700000)
                    .stockQuantity(10)
                    .openTime(LocalDateTime.now().minusDays(1)) 
                    .build());
            
            System.out.println(">> 샘플 상품 데이터가 생성되었습니다.");
        };
    }
}