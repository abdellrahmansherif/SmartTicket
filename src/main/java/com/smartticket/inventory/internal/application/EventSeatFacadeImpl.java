package com.smartticket.inventory.internal.application;

import com.smartticket.inventory.api.EventSeatFacade;
import com.smartticket.inventory.api.ReservableEventSeat;
import com.smartticket.inventory.internal.domain.EventSeat;
import com.smartticket.inventory.internal.domain.EventSeatStatus;
import com.smartticket.inventory.internal.exceptions.EventSeatAlreadyReservedException;
import com.smartticket.inventory.internal.exceptions.EventSeatCannotBeReservedException;
import com.smartticket.inventory.internal.exceptions.EventSeatDoesNotBelongToEventException;
import com.smartticket.inventory.internal.exceptions.EventSeatHoldExpiredException;
import com.smartticket.inventory.internal.exceptions.EventSeatIdsRequiredException;
import com.smartticket.inventory.internal.exceptions.EventSeatNotAvailableException;
import com.smartticket.inventory.internal.exceptions.EventSeatsNotFoundException;
import com.smartticket.inventory.internal.exceptions.InvalidSeatHoldExpirationException;
import com.smartticket.inventory.internal.persistence.EventSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventSeatFacadeImpl
        implements EventSeatFacade {

    private final EventSeatRepository eventSeatRepository;


    @Override
    @Transactional
    public List<ReservableEventSeat> holdSeats(
            UUID eventId,
            List<UUID> eventSeatIds,
            Instant expiresAt
    ) {

        validateSeatIds(eventSeatIds);

        Instant now = Instant.now();

        if (expiresAt == null ||
                !expiresAt.isAfter(now)) {

            throw new InvalidSeatHoldExpirationException();
        }

        List<UUID> uniqueSeatIds =
                eventSeatIds
                        .stream()
                        .distinct()
                        .toList();

        List<EventSeat> eventSeats =
                eventSeatRepository
                        .findAllByIdForUpdate(uniqueSeatIds);

        validateAllSeatsExist(
                eventSeats,
                uniqueSeatIds
        );

        validateSeatsBelongToEvent(
                eventSeats,
                eventId
        );

        /*
         * A seat may still have HELD status even though
         * its hold has expired if the scheduled cleanup
         * has not executed yet.
         */
        eventSeats.forEach(eventSeat -> {

            boolean expiredHold =
                    eventSeat.getStatus()
                            == EventSeatStatus.HELD
                            &&
                            eventSeat.getHeldUntil() != null
                            &&
                            !eventSeat.getHeldUntil()
                                    .isAfter(now);

            if (expiredHold) {
                eventSeat.setStatus(
                        EventSeatStatus.AVAILABLE
                );
                eventSeat.setHeldUntil(null);
            }
        });

        EventSeat unavailableSeat =
                eventSeats
                        .stream()
                        .filter(eventSeat ->
                                eventSeat.getStatus()
                                        != EventSeatStatus.AVAILABLE
                        )
                        .findFirst()
                        .orElse(null);

        if (unavailableSeat != null) {
            throw new EventSeatNotAvailableException(
                    unavailableSeat.getId()
            );
        }

        eventSeats.forEach(eventSeat -> {

            eventSeat.setStatus(
                    EventSeatStatus.HELD
            );

            eventSeat.setHeldUntil(expiresAt);
        });

        return eventSeats
                .stream()
                .map(eventSeat ->
                        new ReservableEventSeat(
                                eventSeat.getId(),
                                eventSeat.getEventId(),
                                eventSeat.getPrice()
                        )
                )
                .toList();
    }


    @Override
    @Transactional
    public void releaseHeldSeats(
            UUID eventId,
            List<UUID> eventSeatIds
    ) {

        validateSeatIds(eventSeatIds);

        List<UUID> uniqueSeatIds =
                eventSeatIds
                        .stream()
                        .distinct()
                        .toList();

        List<EventSeat> eventSeats =
                eventSeatRepository
                        .findAllByIdForUpdate(uniqueSeatIds);

        validateAllSeatsExist(
                eventSeats,
                uniqueSeatIds
        );

        validateSeatsBelongToEvent(
                eventSeats,
                eventId
        );

        eventSeats.forEach(eventSeat -> {

            if (eventSeat.getStatus()
                    == EventSeatStatus.HELD) {

                eventSeat.setStatus(
                        EventSeatStatus.AVAILABLE
                );

                eventSeat.setHeldUntil(null);
            }
        });
    }


    @Override
    @Transactional
    public void makeEventSeatsReserved(
            UUID eventId,
            List<UUID> eventSeatIds
    ) {

        validateSeatIds(eventSeatIds);

        List<UUID> uniqueSeatIds =
                eventSeatIds
                        .stream()
                        .distinct()
                        .toList();

        List<EventSeat> eventSeats =
                eventSeatRepository
                        .findAllByIdForUpdate(uniqueSeatIds);

        validateAllSeatsExist(
                eventSeats,
                uniqueSeatIds
        );

        validateSeatsBelongToEvent(
                eventSeats,
                eventId
        );

        Instant now = Instant.now();

        for (EventSeat eventSeat : eventSeats) {

            if (eventSeat.getStatus()
                    == EventSeatStatus.RESERVED) {

                throw new EventSeatAlreadyReservedException(
                        eventSeat.getId()
                );
            }

            if (eventSeat.getStatus()
                    != EventSeatStatus.HELD) {

                throw new EventSeatCannotBeReservedException(
                        eventSeat.getId(),
                        eventSeat.getStatus()
                );
            }

            boolean expiredHold =
                    eventSeat.getHeldUntil() != null
                            &&
                            !eventSeat
                                    .getHeldUntil()
                                    .isAfter(now);

            if (expiredHold) {

                eventSeat.setStatus(
                        EventSeatStatus.AVAILABLE
                );

                eventSeat.setHeldUntil(null);

                throw new EventSeatHoldExpiredException(
                        eventSeat.getId()
                );
            }
        }

        eventSeats.forEach(
                EventSeat::markAsReserved
        );
    }


    private void validateSeatIds(
            List<UUID> eventSeatIds
    ) {

        if (eventSeatIds == null ||
                eventSeatIds.isEmpty()) {

            throw new EventSeatIdsRequiredException();
        }
    }


    private void validateAllSeatsExist(
            List<EventSeat> eventSeats,
            List<UUID> requestedSeatIds
    ) {

        if (eventSeats.size()
                != requestedSeatIds.size()) {

            throw new EventSeatsNotFoundException();
        }
    }


    private void validateSeatsBelongToEvent(
            List<EventSeat> eventSeats,
            UUID eventId
    ) {

        EventSeat invalidSeat =
                eventSeats
                        .stream()
                        .filter(eventSeat ->
                                !eventSeat
                                        .getEventId()
                                        .equals(eventId)
                        )
                        .findFirst()
                        .orElse(null);

        if (invalidSeat != null) {

            throw new EventSeatDoesNotBelongToEventException(
                    invalidSeat.getId(),
                    eventId
            );
        }
    }
}