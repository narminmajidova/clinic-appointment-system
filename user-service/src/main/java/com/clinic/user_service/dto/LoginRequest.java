package com.clinic.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Username boş ola bilməz")
    private String username;

    @NotBlank(message = "Password boş ola bilməz")
    private String password;
}