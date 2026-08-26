package com.smartticket.venue.internal.persistence;

import com.smartticket.venue.internal.domain.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SectionRepository extends JpaRepository<Section, UUID> {

    List<Section> findByVenueId(UUID venueId);

    boolean existsByVenueIdAndNameIgnoreCase(
            UUID venueId,
            String name
    );
}
