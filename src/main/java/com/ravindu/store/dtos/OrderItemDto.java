package com.ravindu.store.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private OrderProductDto orderProduct;
    private Integer quantity;
    private BigDecimal totalPrice;
}
