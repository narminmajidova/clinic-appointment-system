package com.clinic.clinic_service.controller;

import com.clinic.clinic_service.dto.MedicalServiceRequest;
import com.clinic.clinic_service.dto.MedicalServiceResponse;
import com.clinic.clinic_service.service.MedicalServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class MedicalServiceController {

    private final MedicalServiceService medicalServiceService;

    @PostMapping
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<MedicalServiceResponse> createService(@Valid @RequestBody MedicalServiceRequest request) {
        return ResponseEntity.ok(medicalServiceService.createService(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalServiceResponse> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(medicalServiceService.getServiceById(id));
    }

    @GetMapping
    public ResponseEntity<List<MedicalServiceResponse>> getAllServices() {
        return ResponseEntity.ok(medicalServiceService.getAllServices());
    }

    @GetMapping("/clinic/{clinicId}")
    public ResponseEntity<List<MedicalServiceResponse>> getServicesByClinic(@PathVariable Long clinicId) {
        return ResponseEntity.ok(medicalServiceService.getServicesByClinic(clinicId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<MedicalServiceResponse> updateService(
            @PathVariable Long id,
            @Valid @RequestBody MedicalServiceRequest request
    ) {
        return ResponseEntity.ok(medicalServiceService.updateService(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_MANAGE')")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        medicalServiceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}