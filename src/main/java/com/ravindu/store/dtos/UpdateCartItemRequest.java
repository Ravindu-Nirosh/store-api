package com.ravindu.store.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
    @NotNull(message = "quantity must be provided")
    @Min(value = 0,message = "quantity must be grater than 0")
    private Integer quantity;

}
