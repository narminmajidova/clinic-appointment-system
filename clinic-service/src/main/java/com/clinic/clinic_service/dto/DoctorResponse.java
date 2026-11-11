package com.clinic.clinic_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {

    private Long id;
    private String fullName;
    private String specialization;
    private String phoneNumber;
    private String email;
    private Long userId;
    private Long clinicId;
    private String clinicName;
}