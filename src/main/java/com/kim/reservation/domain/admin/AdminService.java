package com.kim.reservation.domain.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kim.reservation.domain.goods.Goods;
import com.kim.reservation.domain.goods.GoodsRepository;
import com.kim.reservation.domain.waiting.WaitingQueueService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
	
	private final GoodsRepository goodsRepository;
	private final WaitingQueueService waitingQueueService;
	
	/**
     * 대시보드용 상품 및 실시간 대기열 정보 조합
     */
	public List<AdminGoodsDto> getDashboardData(){
		return goodsRepository.findAll().stream().map(goods -> {
	        long waitCount = waitingQueueService.getWaitListCount(goods.getId());
	        long activeCount = waitingQueueService.getActiveUserCount(goods.getId());
	        
	        
	        return new AdminGoodsDto(goods, waitCount, activeCount); 
	        
	    }).collect(Collectors.toList());
	}
	
	/**
     * 재고 강제 업데이트
     */
	@Transactional
	public void updateStock(Long goodsId, int newStock) {
		Goods goods= goodsRepository.findById(goodsId)
				.orElseThrow(() -> new IllegalArgumentException("상품이 없습니다."));
        goods.setStockQuantity(newStock);
	}

}
