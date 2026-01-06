package com.kim.reservation.domain.goods;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoodsService {
	
	private final GoodsRepository goodsRepository;
	
	// 모든 상품 목록 조회
	public List<Goods> findAll(){
		return goodsRepository.findAll();
	}
	
	// 특정 상품 상세 조회
	public Goods findById(Long id) {
		return goodsRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. id=" + id));
	}
}
