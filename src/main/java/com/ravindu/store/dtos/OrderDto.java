package com.ravindu.store.dtos;

import com.ravindu.store.entities.OrderItem;
import com.ravindu.store.entities.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemDto> items;
    private BigDecimal totalPrice;
}
