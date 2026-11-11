package com.clinic.user_service.entity;

public enum Permission {
    // Appointment idarəetməsi
    APPOINTMENT_CREATE,
    APPOINTMENT_READ,
    APPOINTMENT_UPDATE,
    APPOINTMENT_CANCEL,

    // Klinika/Doctor idarəetməsi
    DOCTOR_CREATE,
    DOCTOR_READ,
    DOCTOR_UPDATE,
    DOCTOR_DELETE,

    // Patient idarəetməsi
    PATIENT_READ,
    PATIENT_UPDATE,

    // Admin səlahiyyətləri
    USER_MANAGE
}