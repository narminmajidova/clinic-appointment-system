package com.clinic.clinic_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DoctorRequest {

    @NotBlank(message = "Ad boş ola bilməz")
    private String fullName;

    @NotBlank(message = "İxtisas boş ola bilməz")
    private String specialization;

    private String phoneNumber;

    private String email;

    @NotNull(message = "userId boş ola bilməz")
    private Long userId;

    @NotNull(message = "clinicId boş ola bilməz")
    private Long clinicId;
}