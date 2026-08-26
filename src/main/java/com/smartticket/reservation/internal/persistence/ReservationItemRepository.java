package com.smartticket.reservation.internal.persistence;

import com.smartticket.reservation.internal.domain.ReservationItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReservationItemRepository
        extends JpaRepository<ReservationItem, UUID> {

    List<ReservationItem> findAllByReservationId(UUID reservationId);

    boolean existsByReservationIdAndEventSeatId(
            UUID reservationId,
            UUID eventSeatId
    );
}
