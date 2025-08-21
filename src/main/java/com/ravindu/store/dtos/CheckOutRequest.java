package com.ravindu.store.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckOutRequest {
    @NotNull(message = "cart id is required")
    private UUID cartId;
}
