package com.ravindu.store.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {
    @NotNull(message = "email must be provide")
    @Email
    private String email;

    @NotNull(message = "password must be provide")
    private String password;

}
