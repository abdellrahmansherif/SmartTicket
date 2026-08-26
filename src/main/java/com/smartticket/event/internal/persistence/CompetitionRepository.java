package com.smartticket.event.internal.persistence;

import com.smartticket.event.internal.domain.Competition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompetitionRepository extends JpaRepository<Competition, UUID> {

    boolean existsByNameIgnoreCase(String name);

    Optional<Competition> findByNameIgnoreCase(String name);
}
