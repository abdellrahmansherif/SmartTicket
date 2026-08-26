package com.smartticket.payment.internal.application;

import com.smartticket.order.api.OrderFacade;
import com.smartticket.payment.internal.domain.Payment;
import com.smartticket.payment.internal.domain.PaymentStatus;
import com.smartticket.payment.internal.exceptions.InvalidWebhookPayloadException;
import com.smartticket.payment.internal.exceptions.InvalidWebhookSignatureException;
import com.smartticket.payment.internal.exceptions.OrderNotFoundForPaymentException;
import com.smartticket.payment.internal.exceptions.PaymentCannotBeCompletedException;
import com.smartticket.payment.internal.exceptions.PaymentForOrderNotFoundException;
import com.smartticket.payment.internal.exceptions.PaymentNotFoundException;
import com.smartticket.payment.internal.exceptions.PaymentTransactionNotFoundException;
import com.smartticket.payment.internal.exceptions.WebhookTransactionIdMissingException;
import com.smartticket.payment.internal.gateway.PaymentGateway;
import com.smartticket.payment.internal.persistence.PaymentRepository;
import com.smartticket.payment.internal.web.dto.CreatePaymentRequest;
import com.smartticket.payment.internal.web.dto.PaymentGatewayResponse;
import com.smartticket.payment.internal.web.dto.PaymentResponse;
import com.smartticket.payment.internal.webhook.WebhookSignatureVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final OrderFacade orderFacade;
    private final PaymentRepository paymentRepository;
    private final WebhookSignatureVerifier verifier;
    private final PaymentGateway paymentGateway;
    private final ObjectMapper objectMapper;


    @Transactional
    public PaymentResponse createPayment(
            CreatePaymentRequest request
    ) {

        if (!orderFacade.IsOrderExist(request.orderId())) {
            throw new OrderNotFoundForPaymentException(
                    request.orderId()
            );
        }

        Payment payment = Payment.builder()
                .createdAt(Instant.now())
                .orderId(request.orderId())
                .amount(request.amount())
                .status(PaymentStatus.PENDING)
                .build();

        Payment savedPayment =
                paymentRepository.save(payment);

        PaymentGatewayResponse gatewayResponse =
                paymentGateway.createPayment(
                        savedPayment.getOrderId(),
                        savedPayment.getAmount()
                );

        savedPayment.setTransactionId(
                gatewayResponse.transactionId()
        );

        return toResponse(savedPayment);
    }


    public PaymentResponse getPayment(
            UUID paymentId
    ) {

        Payment payment =
                paymentRepository
                        .findById(paymentId)
                        .orElseThrow(() ->
                                new PaymentNotFoundException(
                                        paymentId
                                )
                        );

        return toResponse(payment);
    }


    public PaymentResponse getPaymentByOrder(
            UUID orderId
    ) {

        Payment payment =
                paymentRepository
                        .findByOrderId(orderId)
                        .orElseThrow(() ->
                                new PaymentForOrderNotFoundException(
                                        orderId
                                )
                        );

        return toResponse(payment);
    }


    @Transactional
    public void handleWebhook(
            String signature,
            String payload
    ) {

        if (!verifier.verify(payload, signature)) {
            throw new InvalidWebhookSignatureException();
        }

        String transactionId =
                extractTransactionId(payload);

        markPaymentAsSuccessful(transactionId);
    }


    private void markPaymentAsSuccessful(
            String transactionId
    ) {

        Payment payment =
                paymentRepository
                        .findByTransactionIdForUpdate(
                                transactionId
                        )
                        .orElseThrow(() ->
                                new PaymentTransactionNotFoundException(
                                        transactionId
                                )
                        );

        /*
         * Payment providers may send the same webhook
         * multiple times.
         *
         * Therefore this operation must be idempotent.
         */
        if (payment.getStatus()
                == PaymentStatus.SUCCESS) {
            return;
        }

        if (payment.getStatus()
                != PaymentStatus.PENDING) {

            throw new PaymentCannotBeCompletedException(
                    payment.getStatus()
            );
        }

        payment.markAsPaid();

        orderFacade.markAsPaid(
                payment.getOrderId()
        );
    }


    private String extractTransactionId(
            String payload
    ) {

        JsonNode root;

        try {

            root = objectMapper.readTree(payload);

        } catch (Exception exception) {

            throw new InvalidWebhookPayloadException();
        }

        JsonNode transactionIdNode =
                root.path("data")
                        .path("object")
                        .path("id");

        if (transactionIdNode.isMissingNode()
                || transactionIdNode.asText().isBlank()) {

            throw new WebhookTransactionIdMissingException();
        }

        return transactionIdNode.asText();
    }


    private PaymentResponse toResponse(
            Payment payment
    ) {

        return new PaymentResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getCreatedAt(),
                payment.getPaidAt()
        );
    }
}