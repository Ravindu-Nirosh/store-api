package com.ravindu.store.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @NotBlank(message = "Name Required")
    public String name;
    public String email;
}
