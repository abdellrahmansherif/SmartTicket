package com.smartticket.inventory.internal.infrastructure;

import com.smartticket.inventory.internal.application.SeatLockManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RedisSeatLockManager implements SeatLockManager {

    private final StringRedisTemplate redisTemplate;

    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT =
            new DefaultRedisScript<>(
                    """
                    if redis.call('get', KEYS[1]) == ARGV[1] then
                        return redis.call('del', KEYS[1])
                    else
                        return 0
                    end
                    """,
                    Long.class
            );
    @Override
    public boolean tryLock(UUID eventId, UUID eventSeatId, String ownerToken, Duration ttl) {
        return false;
    }

    @Override
    public void unlock(UUID eventId, UUID eventSeatId, String ownerToken) {

    }
}
