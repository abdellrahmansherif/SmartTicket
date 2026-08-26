package com.smartticket.payment.internal.gateway;

import com.smartticket.payment.internal.web.dto.PaymentGatewayResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StripePaymentGateway implements PaymentGateway {


    @Override
    public PaymentGatewayResponse createPayment(
            UUID orderId,
            BigDecimal amount
    ) {

        /*
          Here you call Stripe API

          Example:
          Stripe PaymentIntent creation
        */


        String stripeTransactionId =
                "stripe_" + UUID.randomUUID();


        String checkoutUrl =
                "https://stripe.com/checkout/"
                        + stripeTransactionId;


        return new PaymentGatewayResponse(
                stripeTransactionId,
                true,
                checkoutUrl
        );
    }
}
