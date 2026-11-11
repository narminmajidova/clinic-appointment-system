package com.clinic.clinic_service.repository;

import com.clinic.clinic_service.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findByClinicId(Long clinicId);

    boolean existsByUserId(Long userId);
}