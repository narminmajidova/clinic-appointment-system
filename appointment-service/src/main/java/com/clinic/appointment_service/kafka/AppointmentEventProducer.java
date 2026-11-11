package com.clinic.appointment_service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "appointment.created";

    public void publishAppointmentCreated(AppointmentCreatedEvent event) {
        kafkaTemplate.send(TOPIC, event.getAppointmentId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish appointment.created kafka for appointmentId={}: {}",
                                event.getAppointmentId(), ex.getMessage());
                    } else {
                        log.info("Published appointment.created kafka for appointmentId={}",
                                event.getAppointmentId());
                    }
                });
    }
}