package com.smartticket.reservation.internal.application;

import com.smartticket.inventory.api.EventSeatFacade;
import com.smartticket.reservation.api.ReservationDetails;
import com.smartticket.reservation.api.ReservationFacade;
import com.smartticket.reservation.internal.domain.Reservation;
import com.smartticket.reservation.internal.domain.ReservationItem;
import com.smartticket.reservation.internal.domain.ReservationStatus;
import com.smartticket.reservation.internal.exceptions.CancelledReservationCannotBeConfirmedException;
import com.smartticket.reservation.internal.exceptions.ConfirmedReservationCannotBeCancelledException;
import com.smartticket.reservation.internal.exceptions.ExpiredReservationCannotBeCancelledException;
import com.smartticket.reservation.internal.exceptions.ReservationCannotBeCancelledException;
import com.smartticket.reservation.internal.exceptions.ReservationCannotBeConfirmedException;
import com.smartticket.reservation.internal.exceptions.ReservationContainsNoSeatsException;
import com.smartticket.reservation.internal.exceptions.ReservationNotFoundException;
import com.smartticket.reservation.internal.persistence.ReservationItemRepository;
import com.smartticket.reservation.internal.persistence.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationFacadeImpl
        implements ReservationFacade {

    private final ReservationRepository reservationRepository;
    private final ReservationItemRepository reservationItemRepository;
    private final EventSeatFacade eventSeatFacade;


    @Override
    @Transactional
    public void cancelReservation(UUID reservationId) {

        Reservation reservation =
                reservationRepository
                        .findByIdForUpdate(reservationId)
                        .orElseThrow(() ->
                                new ReservationNotFoundException(
                                        reservationId
                                )
                        );

        /*
         * Idempotency.
         */
        if (reservation.getStatus()
                == ReservationStatus.CANCELLED) {
            return;
        }

        if (reservation.getStatus()
                == ReservationStatus.CONFIRMED) {

            throw new ConfirmedReservationCannotBeCancelledException();
        }

        if (reservation.getStatus()
                == ReservationStatus.EXPIRED) {

            throw new ExpiredReservationCannotBeCancelledException();
        }

        if (reservation.getStatus()
                != ReservationStatus.PENDING) {

            throw new ReservationCannotBeCancelledException(
                    reservation.getStatus()
            );
        }

        List<UUID> eventSeatIds =
                reservationItemRepository
                        .findAllByReservationId(
                                reservationId
                        )
                        .stream()
                        .map(
                                ReservationItem::getEventSeatId
                        )
                        .distinct()
                        .toList();

        eventSeatFacade.releaseHeldSeats(
                reservation.getEventID(),
                eventSeatIds
        );

        reservation.setStatus(
                ReservationStatus.CANCELLED
        );
    }


    @Override
    public ReservationDetails getReservation(
            UUID reservationId
    ) {

        Reservation reservation =
                reservationRepository
                        .findById(reservationId)
                        .orElseThrow(() ->
                                new ReservationNotFoundException(
                                        reservationId
                                )
                        );

        return new ReservationDetails(
                reservation.getId(),
                reservation.getUserId(),
                reservation.getTotalAmount(),
                reservation.getStatus().name(),
                reservation.getExpiresAt()
        );
    }


    @Override
    @Transactional
    public void confirmReservation(
            UUID reservationId
    ) {

        Reservation reservation =
                reservationRepository
                        .findByIdForUpdate(reservationId)
                        .orElseThrow(() ->
                                new ReservationNotFoundException(
                                        reservationId
                                )
                        );

        /*
         * Idempotency.
         */
        if (reservation.getStatus()
                == ReservationStatus.CONFIRMED) {
            return;
        }

        if (reservation.getStatus()
                == ReservationStatus.CANCELLED) {

            throw new CancelledReservationCannotBeConfirmedException();
        }

        if (reservation.getStatus()
                != ReservationStatus.PENDING) {

            throw new ReservationCannotBeConfirmedException(
                    reservation.getStatus()
            );
        }

        List<UUID> eventSeatIds =
                reservationItemRepository
                        .findAllByReservationId(
                                reservationId
                        )
                        .stream()
                        .map(
                                ReservationItem::getEventSeatId
                        )
                        .distinct()
                        .toList();

        if (eventSeatIds.isEmpty()) {
            throw new ReservationContainsNoSeatsException();
        }

        eventSeatFacade.makeEventSeatsReserved(
                reservation.getEventID(),
                eventSeatIds
        );

        reservation.confirm();
    }
}