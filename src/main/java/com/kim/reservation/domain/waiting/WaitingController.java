package com.kim.reservation.domain.waiting;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WaitingController {
	
	@GetMapping("/waiting-room")
	public String waitingRoom(@RequestParam Long goodsId, Model model) {
		model.addAttribute("goodsId", goodsId);
		return "waiting";
		
	}

}
