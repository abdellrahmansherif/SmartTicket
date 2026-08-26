package com.smartticket.order.internal.application;

import com.smartticket.order.api.OrderFacade;
import com.smartticket.order.internal.domain.Order;
import com.smartticket.order.internal.domain.OrderStatus;
import com.smartticket.order.internal.exceptions.OrderCannotBeMarkedAsPaidException;
import com.smartticket.order.internal.exceptions.OrderNotFoundException;
import com.smartticket.order.internal.persistence.OrderRepository;
import com.smartticket.reservation.api.ReservationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderFacadeImpl implements OrderFacade {

    private final OrderRepository orderRepository;
    private final ReservationFacade reservationFacade;


    @Override
    public boolean IsOrderExist(UUID id) {
        return orderRepository.existsById(id);
    }


    @Override
    @Transactional
    public void markAsPaid(UUID orderId) {

        Order order =
                orderRepository
                        .findByIdForUpdate(orderId)
                        .orElseThrow(() ->
                                new OrderNotFoundException(orderId)
                        );

        /*
         * Idempotency:
         * receiving the same successful payment callback
         * more than once should not fail.
         */
        if (order.getStatus() == OrderStatus.PAID) {
            return;
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new OrderCannotBeMarkedAsPaidException(
                    order.getStatus().name()
            );
        }

        order.markAsPaid();

        reservationFacade.confirmReservation(
                order.getReservationId()
        );
    }
}