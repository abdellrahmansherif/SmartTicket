package com.smartticket.reservation.internal.web;

import com.smartticket.reservation.internal.application.ReservationService;
import com.smartticket.reservation.internal.web.requests.CreateReservationRequest;
import com.smartticket.reservation.internal.web.requests.ReservationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reserve")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("")
    public ReservationResponse createReservation(@Valid @RequestBody
        CreateReservationRequest request)
    {
        return reservationService.reserve(request);
    }

    @GetMapping("{id}")
    public ReservationResponse getById(@PathVariable UUID id)
    {
        return reservationService.getReservationById(id);
    }

    // 3. Get current user's reservations
    @GetMapping("/me")
    public List<ReservationResponse> getMyReservations() {
        return reservationService.getMyReservations();
    }
    @PatchMapping("/{reservationId}/cancel")
    public ReservationResponse cancelReservation(
            @PathVariable UUID reservationId
    ) {
        return reservationService.cancelReservation(reservationId);
    }

    // 5. Get reservation status

}
