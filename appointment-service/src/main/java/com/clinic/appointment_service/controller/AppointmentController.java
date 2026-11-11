package com.clinic.appointment_service.controller;

import com.clinic.appointment_service.dto.AppointmentRequestDto;
import com.clinic.appointment_service.dto.AppointmentResponseDto;
import com.clinic.appointment_service.entity.AppointmentStatus;
import com.clinic.appointment_service.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasAuthority('APPOINTMENT_CREATE')")
    public ResponseEntity<AppointmentResponseDto> createAppointment(
            @Valid @RequestBody AppointmentRequestDto dto) {

        // Staff (admin, receptionist) istənilən patientId üçün yarada bilər.
        // Sadə PATIENT yalnız öz adına görüş yarada bilər.
        if (!isStaff() && !isSelf(dto.getPatientId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        AppointmentResponseDto response = appointmentService.createAppointment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('APPOINTMENT_READ')")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(@PathVariable Long id) {
        AppointmentResponseDto appointment = appointmentService.getAppointmentById(id);

        if (!isStaff()
                && !isSelf(appointment.getPatientId())
                && !isSelf(appointment.getDoctorId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(appointment);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER_MANAGE', 'PATIENT_UPDATE')")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAuthority('APPOINTMENT_READ')")
    public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsByDoctor(
            @PathVariable Long doctorId) {

        if (!isStaff() && !isSelf(doctorId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAuthority('APPOINTMENT_READ')")
    public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsByPatient(
            @PathVariable Long patientId) {

        if (!isStaff() && !isSelf(patientId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('APPOINTMENT_UPDATE') or hasAuthority('APPOINTMENT_CANCEL')")
    public ResponseEntity<AppointmentResponseDto> updateAppointment(
            @PathVariable Long id, @Valid @RequestBody AppointmentRequestDto dto) {

        AppointmentResponseDto existing = appointmentService.getAppointmentById(id);

        // ADMIN / RECEPTIONIST: məhdudiyyətsiz
        if (isStaff()) {
            return ResponseEntity.ok(appointmentService.updateAppointment(id, dto));
        }

        // DOCTOR: yalnız öz görüşü, həkim və pasiyent dəyişdirilə bilməz
        if (hasAuthority("APPOINTMENT_UPDATE")) {
            if (!isSelf(existing.getDoctorId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            dto.setDoctorId(existing.getDoctorId());
            dto.setPatientId(existing.getPatientId());
            return ResponseEntity.ok(appointmentService.updateAppointment(id, dto));
        }

        // PATIENT: yalnız öz görüşünü ləğv edə bilər
        if (!isSelf(existing.getPatientId()) || dto.getStatus() != AppointmentStatus.CANCELLED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(appointmentService.cancelAppointment(id));
    }

    // "Staff" qaydası yalnız burada təyin olunur: dəyişəndə bir yeri düzəltmək kifayətdir.
    private boolean isStaff() {
        return hasAuthority("USER_MANAGE") || hasAuthority("PATIENT_UPDATE");
    }

    private boolean hasAuthority(String authority) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(authority));
    }

    private boolean isSelf(Long targetId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = (Long) auth.getPrincipal();
        return currentUserId.equals(targetId);
    }
}