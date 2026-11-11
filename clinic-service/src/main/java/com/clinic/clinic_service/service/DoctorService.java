package com.clinic.clinic_service.service;

import com.clinic.clinic_service.dto.DoctorRequest;
import com.clinic.clinic_service.dto.DoctorResponse;
import com.clinic.clinic_service.entity.Clinic;
import com.clinic.clinic_service.entity.Doctor;
import com.clinic.clinic_service.repository.ClinicRepository;
import com.clinic.clinic_service.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final ClinicRepository clinicRepository;

    public DoctorResponse createDoctor(DoctorRequest request) {

        if (doctorRepository.existsByUserId(request.getUserId())) {
            throw new IllegalArgumentException("Bu istifadəçi artıq həkim kimi qeydiyyatdan keçib");
        }

        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new IllegalArgumentException("Klinika tapılmadı: " + request.getClinicId()));

        Doctor doctor = Doctor.builder()
                .fullName(request.getFullName())
                .specialization(request.getSpecialization())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .userId(request.getUserId())
                .clinic(clinic)
                .build();

        doctorRepository.save(doctor);
        return mapToResponse(doctor);
    }

    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Həkim tapılmadı: " + id));
        return mapToResponse(doctor);
    }

    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<DoctorResponse> getDoctorsByClinic(Long clinicId) {
        return doctorRepository.findByClinicId(clinicId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public DoctorResponse updateDoctor(Long id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Həkim tapılmadı: " + id));

        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new IllegalArgumentException("Klinika tapılmadı: " + request.getClinicId()));

        doctor.setFullName(request.getFullName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setPhoneNumber(request.getPhoneNumber());
        doctor.setEmail(request.getEmail());
        doctor.setClinic(clinic);

        doctorRepository.save(doctor);
        return mapToResponse(doctor);
    }

    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new IllegalArgumentException("Həkim tapılmadı: " + id);
        }
        doctorRepository.deleteById(id);
    }

    private DoctorResponse mapToResponse(Doctor doctor) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .fullName(doctor.getFullName())
                .specialization(doctor.getSpecialization())
                .phoneNumber(doctor.getPhoneNumber())
                .email(doctor.getEmail())
                .userId(doctor.getUserId())
                .clinicId(doctor.getClinic().getId())
                .clinicName(doctor.getClinic().getName())
                .build();
    }
}