package com.clinic.clinic_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClinicRequest {

    @NotBlank(message = "Klinika adı boş ola bilməz")
    private String name;

    @NotBlank(message = "Ünvan boş ola bilməz")
    private String address;

    private String phone;

    private String email;

    private String description;
}