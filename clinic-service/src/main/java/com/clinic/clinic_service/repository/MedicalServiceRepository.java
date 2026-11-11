package com.clinic.clinic_service.repository;

import com.clinic.clinic_service.entity.MedicalService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalServiceRepository extends JpaRepository<MedicalService, Long> {

    List<MedicalService> findByClinicId(Long clinicId);
}