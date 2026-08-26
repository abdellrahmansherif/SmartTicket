package com.smartticket.inventory.internal.application;

import java.time.Duration;
import java.util.UUID;

public interface SeatLockManager {
    boolean tryLock(
            UUID eventId,
            UUID eventSeatId,
            String ownerToken,
            Duration ttl
    );

    void unlock(
            UUID eventId,
            UUID eventSeatId,
            String ownerToken
    );
}
