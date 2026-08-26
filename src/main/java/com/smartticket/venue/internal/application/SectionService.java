package com.smartticket.venue.internal.application;

import com.smartticket.venue.internal.domain.Section;
import com.smartticket.venue.internal.domain.Venue;
import com.smartticket.venue.internal.exceptions.SectionAlreadyExistsException;
import com.smartticket.venue.internal.exceptions.SectionNotFoundException;
import com.smartticket.venue.internal.exceptions.VenueNotFoundException;
import com.smartticket.venue.internal.persistence.SectionRepository;
import com.smartticket.venue.internal.persistence.VenueRepository;
import com.smartticket.venue.internal.web.dto.CreateSectionRequest;
import com.smartticket.venue.internal.web.dto.SectionResponse;
import com.smartticket.venue.internal.web.dto.UpdateSectionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SectionService {

    private final SectionRepository sectionRepository;
    private final VenueRepository venueRepository;


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public SectionResponse create(
            UUID venueId,
            CreateSectionRequest request
    ) {

        Venue venue = venueRepository
                .findById(venueId)
                .orElseThrow(() ->
                        new VenueNotFoundException(venueId)
                );

        boolean exists =
                sectionRepository
                        .existsByVenueIdAndNameIgnoreCase(
                                venueId,
                                request.name()
                        );

        if (exists) {
            throw new SectionAlreadyExistsException(
                    request.name()
            );
        }

        Section section = Section.builder()
                .name(request.name())
                .description(request.description())
                .venue(venue)
                .build();

        Section savedSection =
                sectionRepository.save(section);

        return toResponse(savedSection);
    }


    public List<SectionResponse> getByVenue(UUID venueId) {

        if (!venueRepository.existsById(venueId)) {
            throw new VenueNotFoundException(venueId);
        }

        return sectionRepository
                .findByVenueId(venueId)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    public SectionResponse getById(UUID id) {
        return toResponse(findEntity(id));
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public SectionResponse update(
            UUID id,
            UpdateSectionRequest request
    ) {

        Section section = findEntity(id);

        boolean exists =
                sectionRepository
                        .existsByVenueIdAndNameIgnoreCase(
                                section.getVenue().getId(),
                                request.name()
                        );

        boolean sameName =
                section.getName()
                        .equalsIgnoreCase(request.name());

        if (exists && !sameName) {
            throw new SectionAlreadyExistsException(
                    request.name()
            );
        }

        section.setName(request.name());
        section.setDescription(request.description());

        return toResponse(section);
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(UUID id) {

        Section section = findEntity(id);

        sectionRepository.delete(section);
    }


    private Section findEntity(UUID id) {

        return sectionRepository
                .findById(id)
                .orElseThrow(() ->
                        new SectionNotFoundException(id)
                );
    }


    private SectionResponse toResponse(Section section) {

        return new SectionResponse(
                section.getId(),
                section.getName(),
                section.getDescription(),
                section.getVenue().getId()
        );
    }
}