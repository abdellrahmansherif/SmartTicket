package com.smartticket.reservation.internal.application;

import com.smartticket.event.api.EventFacade;
import com.smartticket.identity.api.CurrentUserFacade;
import com.smartticket.inventory.api.EventSeatFacade;
import com.smartticket.inventory.api.ReservableEventSeat;
import com.smartticket.reservation.internal.domain.Reservation;
import com.smartticket.reservation.internal.domain.ReservationItem;
import com.smartticket.reservation.internal.domain.ReservationStatus;
import com.smartticket.reservation.internal.exceptions.ConfirmedReservationCannotBeCancelledException;
import com.smartticket.reservation.internal.exceptions.ExpiredReservationCannotBeCancelledException;
import com.smartticket.reservation.internal.exceptions.ReservationAlreadyCancelledException;
import com.smartticket.reservation.internal.exceptions.ReservationEventNotFoundException;
import com.smartticket.reservation.internal.exceptions.ReservationNotFoundException;
import com.smartticket.reservation.internal.persistence.ReservationItemRepository;
import com.smartticket.reservation.internal.persistence.ReservationRepository;
import com.smartticket.reservation.internal.web.requests.CreateReservationRequest;
import com.smartticket.reservation.internal.web.requests.ReservationItemResponse;
import com.smartticket.reservation.internal.web.requests.ReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final EventFacade eventFacade;
    private final EventSeatFacade eventSeatFacade;
    private final CurrentUserFacade currentUserFacade;

    private final ReservationRepository reservationRepository;
    private final ReservationItemRepository reservationItemRepository;


    @Transactional
    public ReservationResponse reserve(
            CreateReservationRequest request
    ) {

        if (!eventFacade.existsById(request.eventId())) {
            throw new ReservationEventNotFoundException(
                    request.eventId()
            );
        }

        Instant expiresAt =
                Instant.now().plus(
                        10,
                        ChronoUnit.MINUTES
                );

        List<ReservableEventSeat> reservableEventSeats =
                eventSeatFacade.holdSeats(
                        request.eventId(),
                        request.eventSeatIds(),
                        expiresAt
                );

        BigDecimal totalAmount =
                reservableEventSeats
                        .stream()
                        .map(ReservableEventSeat::price)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        Reservation reservation =
                Reservation.builder()
                        .userId(
                                currentUserFacade.getCurrentUserId()
                        )
                        .eventID(
                                request.eventId()
                        )
                        .status(
                                ReservationStatus.PENDING
                        )
                        .totalAmount(totalAmount)
                        .expiresAt(expiresAt)
                        .build();

        Reservation savedReservation =
                reservationRepository.save(reservation);

        List<ReservationItem> reservationItems =
                reservableEventSeats
                        .stream()
                        .map(seat ->
                                ReservationItem.builder()
                                        .reservation(
                                                savedReservation
                                        )
                                        .eventSeatId(
                                                seat.eventSeatId()
                                        )
                                        .price(
                                                seat.price()
                                        )
                                        .build()
                        )
                        .toList();

        List<ReservationItem> savedItems =
                reservationItemRepository
                        .saveAll(reservationItems);

        return toResponse(
                savedReservation,
                savedItems
        );
    }


    public ReservationResponse getReservationById(
            UUID id
    ) {

        Reservation reservation =
                reservationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ReservationNotFoundException(id)
                        );

        List<ReservationItem> items =
                reservationItemRepository
                        .findAllByReservationId(id);

        return toResponse(
                reservation,
                items
        );
    }


    public List<ReservationResponse> getMyReservations() {

        UUID currentUserId =
                currentUserFacade.getCurrentUserId();

        List<Reservation> reservations =
                reservationRepository
                        .findAllByUserId(currentUserId);

        return reservations
                .stream()
                .map(reservation ->
                        toResponse(
                                reservation,
                                reservationItemRepository
                                        .findAllByReservationId(
                                                reservation.getId()
                                        )
                        )
                )
                .toList();
    }


    @Transactional
    public ReservationResponse cancelReservation(
            UUID reservationId
    ) {

        UUID currentUserId =
                currentUserFacade.getCurrentUserId();

        Reservation reservation =
                reservationRepository
                        .findByIdAndUserId(
                                reservationId,
                                currentUserId
                        )
                        .orElseThrow(() ->
                                new ReservationNotFoundException(
                                        reservationId
                                )
                        );

        validateCancellation(reservation);

        List<ReservationItem> items =
                reservationItemRepository
                        .findAllByReservationId(
                                reservationId
                        );

        List<UUID> eventSeatIds =
                items
                        .stream()
                        .map(
                                ReservationItem::getEventSeatId
                        )
                        .toList();

        eventSeatFacade.releaseHeldSeats(
                reservation.getEventID(),
                eventSeatIds
        );

        reservation.setStatus(
                ReservationStatus.CANCELLED
        );

        return toResponse(
                reservation,
                items
        );
    }


    private void validateCancellation(
            Reservation reservation
    ) {

        if (reservation.getStatus()
                == ReservationStatus.CANCELLED) {

            throw new ReservationAlreadyCancelledException();
        }

        if (reservation.getStatus()
                == ReservationStatus.CONFIRMED) {

            throw new ConfirmedReservationCannotBeCancelledException();
        }

        if (reservation.getStatus()
                == ReservationStatus.EXPIRED) {

            throw new ExpiredReservationCannotBeCancelledException();
        }
    }


    private ReservationResponse toResponse(
            Reservation reservation,
            List<ReservationItem> items
    ) {

        List<ReservationItemResponse> itemResponses =
                items
                        .stream()
                        .map(this::toItemResponse)
                        .toList();

        return new ReservationResponse(
                reservation.getId(),
                reservation.getEventID(),
                reservation.getStatus(),
                reservation.getTotalAmount(),
                reservation.getExpiresAt(),
                itemResponses
        );
    }


    private ReservationItemResponse toItemResponse(
            ReservationItem item
    ) {

        return new ReservationItemResponse(
                item.getId(),
                item.getEventSeatId(),
                item.getPrice()
        );
    }
}