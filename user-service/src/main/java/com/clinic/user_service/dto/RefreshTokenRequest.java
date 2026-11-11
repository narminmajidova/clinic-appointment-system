package com.clinic.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {

    @NotBlank(message = "Refresh token boş ola bilməz")
    private String refreshToken;
}