package com.example.order.event;

import com.example.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {

    private String eventType;
    private Long orderId;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long productId;
    private String productName;
    private BigDecimal priceAtPurchase;
    private Instant occurredAt;

    public static OrderCreatedEvent from(Order order) {
        return new OrderCreatedEvent(
                "ORDER_CREATED",
                order.getId(),
                order.getUserId(),
                order.getUserName(),
                order.getUserEmail(),
                order.getProductId(),
                order.getProductName(),
                order.getPriceAtPurchase(),
                Instant.now()
        );
    }
}
