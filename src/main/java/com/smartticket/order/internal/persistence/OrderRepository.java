package com.smartticket.order.internal.persistence;

import com.smartticket.order.internal.domain.Order;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    boolean existsByReservationId(UUID reservationId);

    boolean existsByOrderNumber(UUID orderNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select o
            from Order o
            where o.id = :orderId
            """)
    Optional<Order> findByIdForUpdate(
            @Param("orderId") UUID orderId
    );
}