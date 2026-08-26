package com.smartticket.reservation.internal.persistence;

import com.smartticket.reservation.internal.domain.Reservation;
import com.smartticket.reservation.internal.domain.ReservationStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository
        extends JpaRepository<Reservation, UUID> {

    List<Reservation> findAllByUserId(UUID userId);

    Optional<Reservation> findByIdAndUserId(
            UUID reservationId,
            UUID userId
    );

    List<Reservation> findAllByUserIdAndStatus(
            UUID userId,
            ReservationStatus status
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Reservation r WHERE r.id = :id")
    Optional<Reservation> findByIdForUpdate(@Param("id") UUID id);
}