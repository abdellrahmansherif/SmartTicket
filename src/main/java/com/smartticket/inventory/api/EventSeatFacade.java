package com.smartticket.inventory.api;

import jakarta.persistence.criteria.CriteriaBuilder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface EventSeatFacade {
    List<ReservableEventSeat> holdSeats(
            UUID eventId,
            List<UUID> eventSeatIds,
            Instant expiresAt
    );

    void releaseHeldSeats(
            UUID eventId,
            List<UUID> eventSeatIds
    );

    void makeEventSeatsReserved(UUID eventID, List<UUID> eventSeatIds);
}
