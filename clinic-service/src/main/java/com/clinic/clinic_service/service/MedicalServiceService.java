package com.clinic.clinic_service.service;

import com.clinic.clinic_service.dto.MedicalServiceRequest;
import com.clinic.clinic_service.dto.MedicalServiceResponse;
import com.clinic.clinic_service.entity.Clinic;
import com.clinic.clinic_service.entity.MedicalService;
import com.clinic.clinic_service.repository.ClinicRepository;
import com.clinic.clinic_service.repository.MedicalServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalServiceService {

    private final MedicalServiceRepository medicalServiceRepository;
    private final ClinicRepository clinicRepository;

    public MedicalServiceResponse createService(MedicalServiceRequest request) {

        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new IllegalArgumentException("Klinika tapılmadı: " + request.getClinicId()));

        MedicalService service = MedicalService.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .durationMinutes(request.getDurationMinutes())
                .clinic(clinic)
                .build();

        medicalServiceRepository.save(service);
        return mapToResponse(service);
    }

    public MedicalServiceResponse getServiceById(Long id) {
        MedicalService service = medicalServiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Xidmət tapılmadı: " + id));
        return mapToResponse(service);
    }

    public List<MedicalServiceResponse> getAllServices() {
        return medicalServiceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<MedicalServiceResponse> getServicesByClinic(Long clinicId) {
        return medicalServiceRepository.findByClinicId(clinicId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public MedicalServiceResponse updateService(Long id, MedicalServiceRequest request) {
        MedicalService service = medicalServiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Xidmət tapılmadı: " + id));

        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new IllegalArgumentException("Klinika tapılmadı: " + request.getClinicId()));

        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
        service.setDurationMinutes(request.getDurationMinutes());
        service.setClinic(clinic);

        medicalServiceRepository.save(service);
        return mapToResponse(service);
    }

    public void deleteService(Long id) {
        if (!medicalServiceRepository.existsById(id)) {
            throw new IllegalArgumentException("Xidmət tapılmadı: " + id);
        }
        medicalServiceRepository.deleteById(id);
    }

    private MedicalServiceResponse mapToResponse(MedicalService service) {
        return MedicalServiceResponse.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .price(service.getPrice())
                .durationMinutes(service.getDurationMinutes())
                .clinicId(service.getClinic().getId())
                .build();
    }
}