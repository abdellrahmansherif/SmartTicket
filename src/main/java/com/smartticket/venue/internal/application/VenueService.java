package com.smartticket.venue.internal.application;

import com.smartticket.venue.internal.domain.Venue;
import com.smartticket.venue.internal.exceptions.VenueNotFoundException;
import com.smartticket.venue.internal.persistence.VenueRepository;
import com.smartticket.venue.internal.web.dto.CreateVenueRequest;
import com.smartticket.venue.internal.web.dto.UpdateVenueRequest;
import com.smartticket.venue.internal.web.dto.VenueResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VenueService {

    private final VenueRepository venueRepository;


    @Transactional
    public VenueResponse create(CreateVenueRequest request) {

        Venue venue = Venue.builder()
                .name(request.name())
                .address(request.address())
                .city(request.city())
                .country(request.country())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .build();

        Venue savedVenue =
                venueRepository.save(venue);

        return toResponse(savedVenue);
    }


    public List<VenueResponse> getAll() {

        return venueRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    public VenueResponse getById(UUID id) {
        return toResponse(findEntity(id));
    }


    @Transactional
    public VenueResponse update(
            UUID id,
            UpdateVenueRequest request
    ) {

        Venue venue = findEntity(id);

        venue.setName(request.name());
        venue.setAddress(request.address());
        venue.setCity(request.city());
        venue.setCountry(request.country());
        venue.setLatitude(request.latitude());
        venue.setLongitude(request.longitude());

        return toResponse(venue);
    }


    @Transactional
    public void delete(UUID id) {

        Venue venue = findEntity(id);

        venueRepository.delete(venue);
    }


    private Venue findEntity(UUID id) {

        return venueRepository
                .findById(id)
                .orElseThrow(() ->
                        new VenueNotFoundException(id)
                );
    }


    private VenueResponse toResponse(Venue venue) {

        return new VenueResponse(
                venue.getId(),
                venue.getName(),
                venue.getAddress(),
                venue.getCity(),
                venue.getCountry(),
                venue.getLatitude(),
                venue.getLongitude()
        );
    }
}