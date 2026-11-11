package com.clinic.clinic_service.controller;

import com.clinic.clinic_service.dto.DoctorRequest;
import com.clinic.clinic_service.dto.DoctorResponse;
import com.clinic.clinic_service.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @PreAuthorize("hasAuthority('DOCTOR_CREATE')")
    public ResponseEntity<DoctorResponse> createDoctor(@Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.ok(doctorService.createDoctor(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/clinic/{clinicId}")
    public ResponseEntity<List<DoctorResponse>> getDoctorsByClinic(@PathVariable Long clinicId) {
        return ResponseEntity.ok(doctorService.getDoctorsByClinic(clinicId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('DOCTOR_UPDATE')")
    public ResponseEntity<DoctorResponse> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorRequest request
    ) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DOCTOR_DELETE')")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
}