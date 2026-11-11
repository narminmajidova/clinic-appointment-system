package com.clinic.appointment_service.service;

import com.clinic.appointment_service.dto.AppointmentRequestDto;
import com.clinic.appointment_service.dto.AppointmentResponseDto;
import com.clinic.appointment_service.entity.Appointment;
import com.clinic.appointment_service.entity.AppointmentStatus;
import com.clinic.appointment_service.kafka.AppointmentCreatedEvent;
import com.clinic.appointment_service.kafka.AppointmentEventProducer;
import com.clinic.appointment_service.exception.AppointmentConflictException;
import com.clinic.appointment_service.exception.AppointmentNotFoundException;
import com.clinic.appointment_service.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentEventProducer eventProducer;

    private static final int DEFAULT_DURATION_MINUTES = 30;

    @Transactional
    public AppointmentResponseDto createAppointment(AppointmentRequestDto dto) {
        checkForConflict(dto.getDoctorId(), dto.getAppointmentTime(), null);

        Appointment appointment = Appointment.builder()
                .doctorId(dto.getDoctorId())
                .patientId(dto.getPatientId())
                .medicalServiceId(dto.getMedicalServiceId())
                .appointmentTime(dto.getAppointmentTime())
                .status(AppointmentStatus.PENDING)
                .notes(dto.getNotes())
                .build();

        Appointment saved = appointmentRepository.save(appointment);

        eventProducer.publishAppointmentCreated(
                AppointmentCreatedEvent.builder()
                        .appointmentId(saved.getId())
                        .doctorId(saved.getDoctorId())
                        .patientId(saved.getPatientId())
                        .medicalServiceId(saved.getMedicalServiceId())
                        .appointmentTime(saved.getAppointmentTime())
                        .status(saved.getStatus())
                        .build()
        );

        return toResponseDto(saved);
    }

    public AppointmentResponseDto getAppointmentById(Long id) {
        Appointment appointment = findEntityById(id);
        return toResponseDto(appointment);
    }

    public List<AppointmentResponseDto> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<AppointmentResponseDto> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<AppointmentResponseDto> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Transactional
    public AppointmentResponseDto updateAppointment(Long id, AppointmentRequestDto dto) {
        Appointment appointment = findEntityById(id);

        boolean reactivated = appointment.getStatus() == AppointmentStatus.CANCELLED
                && dto.getStatus() != null
                && dto.getStatus() != AppointmentStatus.CANCELLED;

        if (reactivated
                || !appointment.getAppointmentTime().equals(dto.getAppointmentTime())
                || !appointment.getDoctorId().equals(dto.getDoctorId())) {
            checkForConflict(dto.getDoctorId(), dto.getAppointmentTime(), id);
        }

        appointment.setDoctorId(dto.getDoctorId());
        appointment.setPatientId(dto.getPatientId());
        appointment.setMedicalServiceId(dto.getMedicalServiceId());
        appointment.setAppointmentTime(dto.getAppointmentTime());
        if (dto.getStatus() != null) {
            appointment.setStatus(dto.getStatus());
        }
        appointment.setNotes(dto.getNotes());

        return toResponseDto(appointmentRepository.save(appointment));
    }

    private void checkForConflict(Long doctorId, LocalDateTime requestedTime, Long excludeAppointmentId) {
        LocalDateTime windowStart = requestedTime.minusMinutes(DEFAULT_DURATION_MINUTES - 1);
        LocalDateTime windowEnd = requestedTime.plusMinutes(DEFAULT_DURATION_MINUTES - 1);

        List<Appointment> conflicts = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetween(doctorId, windowStart, windowEnd);

        boolean hasConflict = conflicts.stream()
                .anyMatch(a -> !a.getId().equals(excludeAppointmentId)
                        && a.getStatus() != AppointmentStatus.CANCELLED);

        if (hasConflict) {
            throw new AppointmentConflictException(
                    "Doctor already has an appointment near this time: " + requestedTime);
        }
    }

    private Appointment findEntityById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + id));
    }

    private AppointmentResponseDto toResponseDto(Appointment appointment) {
        return AppointmentResponseDto.builder()
                .id(appointment.getId())
                .doctorId(appointment.getDoctorId())
                .patientId(appointment.getPatientId())
                .medicalServiceId(appointment.getMedicalServiceId())
                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getStatus())
                .createdAt(appointment.getCreatedAt())
                .notes(appointment.getNotes())
                .build();
    }

    @Transactional
    public AppointmentResponseDto cancelAppointment(Long id) {
        Appointment appointment = findEntityById(id);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        return toResponseDto(appointmentRepository.save(appointment));
    }
}