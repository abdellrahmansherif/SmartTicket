package com.smartticket.inventory.internal.application;

import com.smartticket.inventory.internal.domain.EventSeat;
import com.smartticket.inventory.internal.domain.EventSeatStatus;
import com.smartticket.inventory.internal.persistence.EventSeatRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventSeatExpirationService {

    private final EventSeatRepository eventSeatRepository;

    @Scheduled(fixedDelay = 30_000)
    @Transactional
    public void releaseExpiredHolds() {

        Instant now = Instant.now();

        List<EventSeat> expiredSeats =
                eventSeatRepository.findExpiredHeldSeatsForUpdate(
                        EventSeatStatus.HELD,
                        now
                );

        for (EventSeat seat : expiredSeats) {

            seat.setStatus(
                    EventSeatStatus.AVAILABLE
            );

            seat.setHeldUntil(null);
        }
    }
}