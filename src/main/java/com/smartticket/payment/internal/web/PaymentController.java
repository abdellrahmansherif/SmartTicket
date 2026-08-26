package com.smartticket.payment.internal.web;

import com.cloudinary.api.exceptions.NotFound;
import com.smartticket.payment.internal.application.PaymentService;
import com.smartticket.payment.internal.web.dto.CreatePaymentRequest;
import com.smartticket.payment.internal.web.dto.PaymentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody CreatePaymentRequest request
    ) throws NotFound {
        return ResponseEntity.ok(
                paymentService.createPayment(request)
        );
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPayment(
            @PathVariable UUID paymentId
    ) {
        return ResponseEntity.ok(
                paymentService.getPayment(paymentId)
        );
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrder(
            @PathVariable UUID orderId
    ) {
        return ResponseEntity.ok(
                paymentService.getPaymentByOrder(orderId)
        );
    }
}
