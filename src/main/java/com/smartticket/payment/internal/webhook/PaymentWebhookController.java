package com.smartticket.payment.internal.webhook;

import com.smartticket.payment.internal.application.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments/webhook")
@RequiredArgsConstructor
public class PaymentWebhookController {


    private final PaymentService paymentService;


    @PostMapping
    public ResponseEntity<Void> handleWebhook(
            @RequestHeader("Stripe-Signature")
            String signature,

            @RequestBody String payload
    ) {


        paymentService.handleWebhook(
                signature,
                payload
        );


        return ResponseEntity.ok().build();
    }

}
