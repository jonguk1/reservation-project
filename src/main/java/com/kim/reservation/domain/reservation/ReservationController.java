package com.kim.reservation.domain.reservation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kim.reservation.domain.goods.Goods;
import com.kim.reservation.domain.goods.GoodsService;
import com.kim.reservation.domain.waiting.WaitingQueueService;
import com.kim.reservation.global.auth.SessionUser;

import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReservationController {
	
	private final ReservationService reservationService;
	private final ReservationRepository reservationRepository;
	private final WaitingQueueService waitingQueueService;
	private final GoodsService goodsService;
	
	//예약하기
	@GetMapping("/reserve")
	public String reservePage(@RequestParam Long goodsId, HttpSession session) throws UnsupportedEncodingException {
	    
		Goods goods = goodsService.findById(goodsId);
	    
	    //서버 시간 기준으로 아직 오픈 전이라면 예약을 막음
	    if (LocalDateTime.now().isBefore(goods.getOpenTime())) {
	        return "redirect:/goods/" + goodsId + "?error=notyet";
	    }
		
	    // 1. 세션 확인 (유저가 없으면 로그인으로)
	    SessionUser user = (SessionUser) session.getAttribute("user");
	    if (user == null) {
	        return "redirect:/oauth2/authorization/google";
	    }

	    // 2. Redis 대기열 순번 확인
	    Long rank = waitingQueueService.getRank(goodsId, user.getId().toString());

	    // 대기열에 없거나, 내 앞 인원이 0보다 크면 대기실로 돌려보냄
	    if (rank == null || rank > 0) {
	        // 비정상적인 접근(직접 주소창에 /reserve 입력 등) 차단
	        return "redirect:/waiting-room?goodsId=" + goodsId;
	    }

	    try {
	        // 3. DB 재고 감소 및 예약 내역 저장
	        reservationService.reserve(user.getId(), goodsId);
	        
	        // 4.예약 성공 시 Redis 대기열에서 해당 유저 삭제
	        waitingQueueService.removeActiveUser(goodsId, user.getId().toString());

	        // 예약 성공 시 상품 목록으로 이동
	        return "redirect:/goods?success=true";

	    } catch (Exception e) {
	    	// 예약 실패
	        waitingQueueService.removeActiveUser(goodsId, user.getId().toString());
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
