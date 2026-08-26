package com.smartticket.venue.internal.application;

import com.smartticket.venue.internal.domain.Seat;
import com.smartticket.venue.internal.domain.Section;
import com.smartticket.venue.internal.exceptions.SeatAlreadyExistsException;
import com.smartticket.venue.internal.exceptions.SeatNotFoundException;
import com.smartticket.venue.internal.exceptions.SectionNotFoundException;
import com.smartticket.venue.internal.persistence.SeatRepository;
import com.smartticket.venue.internal.persistence.SectionRepository;
import com.smartticket.venue.internal.web.dto.CreateSeatRequest;
import com.smartticket.venue.internal.web.dto.SeatResponse;
import com.smartticket.venue.internal.web.dto.UpdateSeatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeatService {

    private final SeatRepository seatRepository;
    private final SectionRepository sectionRepository;


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public SeatResponse create(
            UUID sectionId,
            CreateSeatRequest request
    ) {

        Section section = sectionRepository
                .findById(sectionId)
                .orElseThrow(() ->
                        new SectionNotFoundException(sectionId)
                );

        boolean exists =
                seatRepository
                        .existsBySectionIdAndRowLabelIgnoreCaseAndSeatNumberIgnoreCase(
                                sectionId,
                                request.rowLabel(),
                                request.seatNumber()
                        );

        if (exists) {
            throw new SeatAlreadyExistsException(
                    request.rowLabel(),
                    request.seatNumber()
            );
        }

        Seat seat = Seat.builder()
                .rowLabel(request.rowLabel())
                .seatNumber(request.seatNumber())
                .section(section)
                .build();

        Seat savedSeat = seatRepository.save(seat);

        return toResponse(savedSeat);
    }


    public List<SeatResponse> getBySection(UUID sectionId) {

        if (!sectionRepository.existsById(sectionId)) {
            throw new SectionNotFoundException(sectionId);
        }

        return seatRepository
                .findBySectionId(sectionId)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    public SeatResponse getById(UUID id) {
        return toResponse(findEntity(id));
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public SeatResponse update(
            UUID id,
            UpdateSeatRequest request
    ) {

        Seat seat = findEntity(id);

        boolean exists =
                seatRepository
                        .existsBySectionIdAndRowLabelIgnoreCaseAndSeatNumberIgnoreCase(
                                seat.getSection().getId(),
                                request.rowLabel(),
                                request.seatNumber()
                        );

        boolean sameSeat =
                seat.getRowLabel()
                        .equalsIgnoreCase(request.rowLabel())
                        &&
                        seat.getSeatNumber()
                                .equalsIgnoreCase(request.seatNumber());

        if (exists && !sameSeat) {
            throw new SeatAlreadyExistsException(
                    request.rowLabel(),
                    request.seatNumber()
            );
        }

        seat.setRowLabel(request.rowLabel());
        seat.setSeatNumber(request.seatNumber());

        return toResponse(seat);
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(UUID id) {

        Seat seat = findEntity(id);

        seatRepository.delete(seat);
    }


    private Seat findEntity(UUID id) {

        return seatRepository
                .findById(id)
                .orElseThrow(() ->
                        new SeatNotFoundException(id)
                );
    }


    private SeatResponse toResponse(Seat seat) {

        return new SeatResponse(
                seat.getId(),
                seat.getRowLabel(),
                seat.getSeatNumber(),
                seat.getSection().getId()
        );
    }
}