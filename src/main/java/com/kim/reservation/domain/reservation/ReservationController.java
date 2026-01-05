package com.kim.reservation.domain.reservation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kim.reservation.global.auth.SessionUser;

import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReservationController {
	
	private final ReservationService reservationService;
	private final ReservationRepository reservationRepository;
	
	//예약하기
	@GetMapping("/reserve")
	public String reserve(@RequestParam Long goodsId,HttpSession session) throws UnsupportedEncodingException {
		
		//세션에서 현재 로그인한 유저 정보를 꺼내오기
		SessionUser user = (SessionUser) session.getAttribute("user");
		
		//로그인이 안되면 로그인 페이지로 강제 이동
		if(user == null) {
			return "redirect:/oauth2/authorization/google";
		}
		
		try {
			//실제 로그인한 유저의 id와 클릭한 상품 id를 서비스에 전달
			reservationService.reserve(user.getId(), goodsId);
			
			//예약 성공
			return "redirect:/goods?success=true";
		}catch (Exception e) {
			//예약 실패
			return "redirect:/goods?error=" + URLEncoder.encode(e.getMessage(), "UTF-8");
		}
		
	}
	
	//나의 예약 목록 보기
	@GetMapping("/my-reservations")
	public String myReservations(HttpSession session,Model model) {
		
		//세션에서 현재 로그인한 유저 정보를 꺼내오기
		SessionUser user = (SessionUser) session.getAttribute("user");
		
		//로그인이 안되면 로그인 페이지로 강제 이동
		if(user == null) {
			return "redirect:/oauth2/authorization/google";
		}
		
		// 로그인한 유저의 예약 내역만 가져와서 모델에 담기
		List<Reservation> myReservations =reservationRepository.findByUserId(user.getId());
		model.addAttribute("reservations",myReservations);
		
		return "myReservations";
		
	}
	
	
	

}
