package com.kim.reservation.domain.goods;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GoodsController {
	
	private final GoodsRepository goodsRepository;
	
	@GetMapping("/goods")
	public String list(Model model) {
		List<Goods> goodsList = goodsRepository.findAll();
		model.addAttribute("goods",goodsList);
		return "goodsList";
	}

}
