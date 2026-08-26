package com.smartticket.order.api;

import java.util.UUID;

public interface OrderFacade {
    boolean IsOrderExist(UUID id);
    void markAsPaid(UUID orderId);
}
