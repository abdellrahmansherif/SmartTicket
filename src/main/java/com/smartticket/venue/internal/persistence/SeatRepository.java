package com.smartticket.venue.internal.persistence;
import com.smartticket.venue.internal.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SeatRepository
        extends JpaRepository<Seat, UUID> {

    List<Seat> findBySectionId(UUID sectionId);

    boolean existsBySectionIdAndRowLabelIgnoreCaseAndSeatNumberIgnoreCase(
            UUID sectionId,
            String rowLabel,
            String seatNumber
    );
}
