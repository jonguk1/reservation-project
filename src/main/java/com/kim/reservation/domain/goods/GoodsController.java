package com.kim.reservation.domain.goods;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GoodsController {
	
	private final GoodsService goodsService;
	
	//상품 리스트
	@GetMapping("/goods")
	public String list(Model model) {
		List<Goods> goodsList = goodsService.findAll();
		model.addAttribute("goods",goodsList);
		return "goodsList";
	}

	
	//상품 상세 페이지
	@GetMapping("/goods/{id}")
	public String goodsDetail(@PathVariable Long id,Model model) {
		Goods goods= goodsService.findById(id);
		model.addAttribute("item",goods);
		return "goodsDetail";
	}
}
