package com.ravindu.store.dtos;

import com.ravindu.store.validation.Lowercase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {

    @NotBlank(message = "name is required")
    @Size(max = 255,message = "name must be less than 255 characters")
    private String name;

    @NotBlank(message = "email is required")
    @Email(message = "email is not valid")
    @Lowercase(message = "email must be in lowercase")
    private String email;

    @NotBlank
    @Size(min = 3,max = 25,message = "password must be 3-25 characters long")
    private String password;
}
