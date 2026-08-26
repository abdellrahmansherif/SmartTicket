package com.smartticket.inventory.internal.persistence;

import com.smartticket.inventory.internal.domain.EventSeat;
import com.smartticket.inventory.internal.domain.EventSeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface EventSeatRepository extends JpaRepository<EventSeat, UUID> {

    boolean existsByEventIdAndSeatId(UUID eventId, UUID seatId);

    List<EventSeat> findAllByEventId(UUID eventId);

    List<EventSeat> findAllByEventIdAndStatus(
            UUID eventId,
            EventSeatStatus status
    );
    List<EventSeat> findAllByIdIn(List<UUID> ids);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT es
        FROM EventSeat es
        WHERE es.id IN :seatIds
        ORDER BY es.id
    """)
    List<EventSeat> findAllByIdForUpdate(
            @Param("seatIds") List<UUID> seatIds
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT es
    FROM EventSeat es
    WHERE es.status = :status
      AND es.heldUntil IS NOT NULL
      AND es.heldUntil <= :now
    ORDER BY es.id
""")
    List<EventSeat> findExpiredHeldSeatsForUpdate(
            @Param("status") EventSeatStatus status,
            @Param("now") Instant now
    );
}
