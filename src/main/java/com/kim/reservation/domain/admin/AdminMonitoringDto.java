package com.kim.reservation.domain.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminMonitoringDto {
	private String goodsName;
	private int totalStock; // 현재 남은 재고
	private Long pendingCount;// 현재 결제 대기 중인 사람 (선점)
	private Long confirmedCount; // 결제 완료한 사람
	private Long waitingCount; //대기열에서 기다리는 사람 수

}
