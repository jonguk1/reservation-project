package com.kim.reservation.domain.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {
	private final AdminService adminService;
	
	@GetMapping("/monitoring/{goodsId}")
	public String monitoring(@PathVariable Long goodsId, Model model) {
		model.addAttribute("data", adminService.getMonitoringData(goodsId));
		return "monitoring";
	}

}
