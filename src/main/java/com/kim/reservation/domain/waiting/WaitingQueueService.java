package com.kim.reservation.domain.waiting;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WaitingQueueService {
    
    private final RedisTemplate<String, String> redisTemplate;
    private final String QUEUE_KEY_PREFIX = "wait-list:";
    private final String ACTIVE_KEY_PREFIX = "active-users:"; // 현재 입장 중인 유저 키
    private final int MAX_ALLOW_COUNT = 5;
    
    // 대기열에 유저 추가
    public Long registerQueue(Long goodsId, String userId) {
        String queueKey = QUEUE_KEY_PREFIX + goodsId;
        redisTemplate.opsForZSet().add(queueKey, userId, System.currentTimeMillis());
        return getRank(goodsId, userId);
    }
    
    // 현재 순번 조회 전용
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
    
    // 예약 완료 후 완전히 제거
    public void removeActiveUser(Long goodsId, String userId) {
        String activeKey = ACTIVE_KEY_PREFIX + goodsId;
        redisTemplate.opsForSet().remove(activeKey, userId);
    }
}
