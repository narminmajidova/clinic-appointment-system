package com.clinic.user_service.entity;

import lombok.Getter;
import java.util.Set;
import static com.clinic.user_service.entity.Permission.*;

@Getter
public enum Role {

    PATIENT(Set.of(
            APPOINTMENT_CREATE,
            APPOINTMENT_READ,
            APPOINTMENT_CANCEL
    )),

    DOCTOR(Set.of(
            APPOINTMENT_READ,
            APPOINTMENT_UPDATE,
            PATIENT_READ
    )),

    RECEPTIONIST(Set.of(
            APPOINTMENT_CREATE,
            APPOINTMENT_READ,
            APPOINTMENT_UPDATE,
            APPOINTMENT_CANCEL,
            PATIENT_READ,
            PATIENT_UPDATE,
            DOCTOR_READ
    )),

    ADMIN(Set.of(
            APPOINTMENT_CREATE,
            APPOINTMENT_READ,
            APPOINTMENT_UPDATE,
            APPOINTMENT_CANCEL,
            DOCTOR_CREATE,
            DOCTOR_READ,
            DOCTOR_UPDATE,
            DOCTOR_DELETE,
            PATIENT_READ,
            PATIENT_UPDATE,
            USER_MANAGE
    ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}