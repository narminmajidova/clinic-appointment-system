package com.clinic.clinic_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicResponse {

    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String description;
}