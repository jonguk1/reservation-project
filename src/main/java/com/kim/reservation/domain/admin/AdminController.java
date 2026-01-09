package com.kim.reservation.domain.admin;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {
	
	private final AdminService adminService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("goodsList", adminService.getDashboardData());
        return "dashboard";
    }

    @PostMapping("/update-stock")
    public String updateStock(@RequestParam Long goodsId, @RequestParam int stock) {
        adminService.updateStock(goodsId, stock);
        return "redirect:/dashboard";
    }

}
