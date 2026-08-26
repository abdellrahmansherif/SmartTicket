package com.smartticket.venue.internal.persistence;

import com.smartticket.venue.internal.domain.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface VenueRepository extends JpaRepository<Venue, UUID> {

    List<Venue> findByCityIgnoreCase(String city);
}

