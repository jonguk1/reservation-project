package com.kim.reservation.domain.waiting;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WaitingQueueService {
    
    private final RedisTemplate<String, String> redisTemplate;
    private final String QUEUE_KEY_PREFIX = "wait-list:";
    private final String ACTIVE_KEY_PREFIX = "active-user:"; // 현재 입장 중인 유저 키
    private final int MAX_ALLOW_COUNT = 5;
    
    /**
     * 대기열에 유저 추가
     */
    public Long registerQueue(Long goodsId, String userId) {
        String queueKey = QUEUE_KEY_PREFIX + goodsId;
        String activeKey = ACTIVE_KEY_PREFIX + goodsId;

        //이미 결제 중인 유저라면 대기열에 추가하지 않고 바로 0번 반환
        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(activeKey, userId))) {
            return 0L;
        }

        redisTemplate.opsForZSet().add(queueKey, userId, System.currentTimeMillis());
        return getRank(goodsId, userId);
    }
    
    /**
     * 현재 순번 조회 전용
     */
    public Long getRank(Long goodsId, String userId) {
        String queueKey = QUEUE_KEY_PREFIX + goodsId;
        String activeKey = ACTIVE_KEY_PREFIX + goodsId;
        
        // 1. 입장 허가 상태 확인
        if(Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(activeKey, userId))) {
            return 0L;
        }
        
        // 2. 대기열 순번 확인
        Long rank = redisTemplate.opsForZSet().rank(queueKey, userId);
        
        // 대기열에도 없고 입장 상태도 아니라면? (비정상 접근 대비)
        if (rank == null) return null; 

        // 3. 현재 입장 인원 확인 
        Long activeCount = redisTemplate.opsForSet().size(activeKey);
        if (activeCount == null) activeCount = 0L;
        
        // 4. 입장 조건 확인
        if(rank == 0 && activeCount < MAX_ALLOW_COUNT) {
            redisTemplate.opsForSet().add(activeKey, userId);     
            redisTemplate.opsForZSet().remove(queueKey, userId);
            return 0L;
        }
        
        //대기 순번 반환
        return rank + 1;
    }
    
    
    /**
     * 예약 완료 후 완전히 제거
     */
    public void removeActiveUser(Long goodsId, String userId) {
    	String queueKey = QUEUE_KEY_PREFIX + goodsId;
        String activeKey = ACTIVE_KEY_PREFIX + goodsId;
        
        redisTemplate.opsForSet().remove(activeKey, userId);
        redisTemplate.opsForZSet().remove(queueKey, userId);
    }
    
    /**
     * 재고 소진
     */
    public void processSoldOut(Long goodsId) {
    	String waitListKey = "wait-list:" + goodsId;
    	
    	// 1. 대기열(ZSet) 자체를 삭제하여 대기 중인 모든 유저 해산
    	redisTemplate.delete(waitListKey);
    	
    	log.info("상품 {} 재고 소진으로 대기열이 폐쇄되었습니다.", goodsId);
    	
    }
    
    /**
     * 특정 상품의 대기열에 현재 몇 명이 있는지 조회
     */
    public long getWaitListCount(Long goodsId) {
    	String key = "wait-list:" + goodsId;
    	Long count = redisTemplate.opsForZSet().zCard(key);
    	return count != null ? count : 0L;
    }
    
    /**
     * 현재 예약/결제 진행 중인(활성 상태) 유저 수 조회
     */
    public long getActiveUserCount(Long goodsId) {
        String key = "active-user:" + goodsId;
        Long count = redisTemplate.opsForSet().size(key);
        return count != null ? count : 0L;
    }
    
    
}
