package com.smartticket.order.internal.web;

import com.smartticket.order.internal.application.OrderService;
import com.smartticket.order.internal.web.dto.CreateOrderRequest;
import com.smartticket.order.internal.web.dto.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(
            @Valid @RequestBody CreateOrderRequest request
    ) {
        return orderService.createOrder(request);
    }

    // Get Order by ID
    @GetMapping("/{orderId}")
    public OrderResponse getOrder(
            @PathVariable UUID orderId
    ) {
        return orderService.getOrder(orderId);
    }

    // Get current user's orders
    @PostMapping("/{orderId}/cancel")
    public OrderResponse cancelOrder(
            @PathVariable UUID orderId
    ) {
        return orderService.cancelOrder(orderId);
    }
}
