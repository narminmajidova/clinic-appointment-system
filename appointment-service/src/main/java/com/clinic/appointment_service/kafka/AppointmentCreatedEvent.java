package com.clinic.appointment_service.kafka;

import com.clinic.appointment_service.entity.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentCreatedEvent {

    private Long appointmentId;
    private Long doctorId;
    private Long patientId;
    private Long medicalServiceId;
    private LocalDateTime appointmentTime;
    private AppointmentStatus status;
}