package com.ravindu.store.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class ProductDto {

    private Long id;

    @NotNull(message = "product name is required")
    private String name;

    @NotNull(message = "product price is required")
    @DecimalMin(value = "0.0",inclusive = false,message = "price must be grater than 0")
    private BigDecimal price;

    @NotNull(message = "product description is required")
    private String description;

    @NotNull(message = "product categoryId is required")
    private Byte categoryId;
}
