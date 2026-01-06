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
        
        //1.내가 이미 '입장 허가' 상태인지 확인
        if(Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(activeKey, userId))) {
        	return 0L;// 이미 입장했으므로 0등으로 취급
        }
        
        // 2. 현재 입장해서 작업 중인 인원 수 확인
        Long activeCount = redisTemplate.opsForSet().size(activeKey);
        
        // 3. 내 순서가 0번(1등)이고, 입장 인원이 여유가 있다면 '입장 처리'
        Long rank= redisTemplate.opsForZSet().rank(queueKey, userId);
        if(rank != null && rank == 0 &&(activeCount != null && activeCount < MAX_ALLOW_COUNT)) {
        	// 입장 허가 명단에 추가
        	redisTemplate.opsForSet().add(activeKey, userId);
        	// 대기열에선 삭제
        	redisTemplate.opsForSet().remove(activeKey, userId);
        }
        // 내 앞 대기자 + 현재 작업자 수로 표시
        return (rank != null) ? rank + activeCount : null;
    }
    
    // 예약 완료 후 완전히 제거
    public void removeActiveUser(Long goodsId, String userId) {
        String activeKey = ACTIVE_KEY_PREFIX + goodsId;
        redisTemplate.opsForZSet().remove(activeKey, userId);
    }
}
