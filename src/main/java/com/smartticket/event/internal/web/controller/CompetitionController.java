package com.smartticket.event.internal.web.controller;

import com.smartticket.event.internal.application.CompetitionService;
import com.smartticket.event.internal.web.request.CreateCompetitionRequest;
import com.smartticket.event.internal.web.request.UpdateCompetitionRequest;
import com.smartticket.event.internal.web.response.CompetitionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CompetitionController {

    private final CompetitionService competitionService;

    @PostMapping("/api/admin/competitions")
    @ResponseStatus(HttpStatus.CREATED)
    public CompetitionResponse createCompetition(
            @Valid @RequestBody CreateCompetitionRequest request
    ) {
        return competitionService.create(request);
    }

    @GetMapping("/api/competitions")
    public List<CompetitionResponse> getAllCompetitions() {
        return competitionService.getAll();
    }

    @GetMapping("/api/competitions/{id}")
    public CompetitionResponse getCompetitionById(
            @PathVariable UUID id
    ) {
        return competitionService.getById(id);
    }

    @PutMapping("/api/admin/competitions/{id}")
    public CompetitionResponse updateCompetition(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCompetitionRequest request
    ) {
        return competitionService.update(id, request);
    }

    @DeleteMapping("/api/admin/competitions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompetition(@PathVariable UUID id) {
        competitionService.delete(id);
    }
}
