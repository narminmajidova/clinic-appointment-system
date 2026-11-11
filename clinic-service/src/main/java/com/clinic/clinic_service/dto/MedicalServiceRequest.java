package com.clinic.clinic_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MedicalServiceRequest {

    @NotBlank(message = "Xidmət adı boş ola bilməz")
    private String name;

    private String description;

    @NotNull(message = "Qiymət boş ola bilməz")
    @Positive(message = "Qiymət müsbət olmalıdır")
    private BigDecimal price;

    @NotNull(message = "Müddət boş ola bilməz")
    @Positive(message = "Müddət müsbət olmalıdır")
    private Integer durationMinutes;

    @NotNull(message = "clinicId boş ola bilməz")
    private Long clinicId;
}