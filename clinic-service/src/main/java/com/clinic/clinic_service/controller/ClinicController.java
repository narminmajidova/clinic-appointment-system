package com.clinic.clinic_service.controller;

import com.clinic.clinic_service.dto.ClinicRequest;
import com.clinic.clinic_service.dto.ClinicResponse;
import com.clinic.clinic_service.service.ClinicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clinics")
@RequiredArgsConstructor
public class ClinicController {

    private final ClinicService clinicService;

    @PostMapping
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<ClinicResponse> createClinic(@Valid @RequestBody ClinicRequest request) {
        return ResponseEntity.ok(clinicService.createClinic(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicResponse> getClinicById(@PathVariable Long id) {
        return ResponseEntity.ok(clinicService.getClinicById(id));
    }

    @GetMapping
    public ResponseEntity<List<ClinicResponse>> getAllClinics() {
        return ResponseEntity.ok(clinicService.getAllClinics());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<ClinicResponse> updateClinic(
            @PathVariable Long id,
            @Valid @RequestBody ClinicRequest request
    ) {
        return ResponseEntity.ok(clinicService.updateClinic(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<Void> deleteClinic(@PathVariable Long id) {
        clinicService.deleteClinic(id);
        return ResponseEntity.noContent().build();
    }
}