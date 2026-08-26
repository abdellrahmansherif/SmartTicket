package com.smartticket.payment.internal.webhook;


import org.springframework.stereotype.Component;

@Component
public class WebhookSignatureVerifier {


    public boolean verify(
            String payload,
            String signature
    ) {


        /*
          In real Stripe:

          Stripe-Signature header
          +
          webhook secret

          are used here.
        */


        if(signature == null || signature.isBlank()){
            return false;
        }


        return true;
    }

}
