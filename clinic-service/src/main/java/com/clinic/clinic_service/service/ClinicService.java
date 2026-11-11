package com.clinic.clinic_service.service;

import com.clinic.clinic_service.dto.ClinicRequest;
import com.clinic.clinic_service.dto.ClinicResponse;
import com.clinic.clinic_service.entity.Clinic;
import com.clinic.clinic_service.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClinicService {

    private final ClinicRepository clinicRepository;

    public ClinicResponse createClinic(ClinicRequest request) {
        Clinic clinic = Clinic.builder()
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .email(request.getEmail())
                .description(request.getDescription())
                .build();

        clinicRepository.save(clinic);
        return mapToResponse(clinic);
    }

    public ClinicResponse getClinicById(Long id) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Klinika tapılmadı: " + id));
        return mapToResponse(clinic);
    }

    public List<ClinicResponse> getAllClinics() {
        return clinicRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ClinicResponse updateClinic(Long id, ClinicRequest request) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Klinika tapılmadı: " + id));

        clinic.setName(request.getName());
        clinic.setAddress(request.getAddress());
        clinic.setPhone(request.getPhone());
        clinic.setEmail(request.getEmail());
        clinic.setDescription(request.getDescription());

        clinicRepository.save(clinic);
        return mapToResponse(clinic);
    }

    public void deleteClinic(Long id) {
        if (!clinicRepository.existsById(id)) {
            throw new IllegalArgumentException("Klinika tapılmadı: " + id);
        }
        clinicRepository.deleteById(id);
    }

    private ClinicResponse mapToResponse(Clinic clinic) {
        return ClinicResponse.builder()
                .id(clinic.getId())
                .name(clinic.getName())
                .address(clinic.getAddress())
                .phone(clinic.getPhone())
                .email(clinic.getEmail())
                .description(clinic.getDescription())
                .build();
    }
}