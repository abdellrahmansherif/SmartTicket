package com.smartticket.order.internal.application;

import com.smartticket.identity.api.CurrentUserFacade;
import com.smartticket.order.internal.domain.Order;
import com.smartticket.order.internal.domain.OrderStatus;
import com.smartticket.order.internal.exceptions.OrderAlreadyCancelledException;
import com.smartticket.order.internal.exceptions.OrderAlreadyExistsException;
import com.smartticket.order.internal.exceptions.OrderNotFoundException;
import com.smartticket.order.internal.exceptions.OrderReservationAccessDeniedException;
import com.smartticket.order.internal.exceptions.PaidOrderCannotBeCancelledException;
import com.smartticket.order.internal.exceptions.ReservationExpiredException;
import com.smartticket.order.internal.exceptions.ReservationNotOrderableException;
import com.smartticket.order.internal.persistence.OrderRepository;
import com.smartticket.order.internal.web.dto.CreateOrderRequest;
import com.smartticket.order.internal.web.dto.OrderResponse;
import com.smartticket.reservation.api.ReservationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final ReservationFacade reservationFacade;
    private final CurrentUserFacade currentUserFacade;
    private final OrderRepository orderRepository;


    @PreAuthorize("hasRole('CUSTOMER')")
    @Transactional
    public OrderResponse createOrder(
            CreateOrderRequest request
    ) {

        UUID currentUserId =
                currentUserFacade.getCurrentUserId();

        var reservation =
                reservationFacade.getReservation(
                        request.reservationId()
                );

        if (!reservation.userId().equals(currentUserId)) {
            throw new OrderReservationAccessDeniedException();
        }

        if (!reservation.status().equals("PENDING")) {
            throw new ReservationNotOrderableException(
                    reservation.status()
            );
        }

        Instant now = Instant.now();

        if (reservation.expiresAt() == null
                || !reservation.expiresAt().isAfter(now)) {

            throw new ReservationExpiredException();
        }

        boolean orderExists =
                orderRepository.existsByReservationId(
                        reservation.id()
                );

        if (orderExists) {
            throw new OrderAlreadyExistsException(
                    reservation.id()
            );
        }

        Order order = Order.builder()
                .reservationId(reservation.id())
                .totalAmount(reservation.totalAmount())
                .createdAt(now)
                .status(OrderStatus.PENDING)
                .orderNumber(generateOrderNumber())
                .build();

        Order savedOrder =
                orderRepository.save(order);

        return toResponse(savedOrder);
    }


    public OrderResponse getOrder(UUID orderId) {

        return toResponse(
                findEntity(orderId)
        );
    }


    @Transactional
    @PreAuthorize("hasRole('CUSTOMER')")
    public OrderResponse cancelOrder(UUID orderId) {

        Order order = findEntity(orderId);

        /*
         * Validate BEFORE changing the status.
         */
        if (order.getStatus() == OrderStatus.PAID) {
            throw new PaidOrderCannotBeCancelledException();
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new OrderAlreadyCancelledException();
        }

        reservationFacade.cancelReservation(
                order.getReservationId()
        );

        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelledAt(Instant.now());

        return toResponse(order);
    }


    private Order findEntity(UUID orderId) {

        return orderRepository
                .findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException(orderId)
                );
    }


    private String generateOrderNumber() {

        return "ORD-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }


    private OrderResponse toResponse(Order order) {

        return new OrderResponse(
                order.getId(),
                order.getReservationId(),
                order.getOrderNumber(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getPaidAt(),
                order.getCancelledAt()
        );
    }
}