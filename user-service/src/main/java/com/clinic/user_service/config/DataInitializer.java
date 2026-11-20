package com.clinic.user_service.config;

import com.clinic.user_service.entity.Role;
import com.clinic.user_service.entity.User;
import com.clinic.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createUser("admin_aysel", "aysel123", "aysel@clinic.az", "Aysel Məmmədova", "+994501111111", Role.ADMIN);
        createUser("doctor_kamran", "kamran123", "kamran@clinic.az", "Dr. Kamran Əliyev", "+994502222222", Role.DOCTOR);
        createUser("doctor_leyla", "leyla123", "leyla@clinic.az", "Dr. Leyla Hüseynova", "+994503333333", Role.DOCTOR);
        createUser("reception_nigar", "nigar123", "nigar@clinic.az", "Nigar Rəhimova", "+994504444444", Role.RECEPTIONIST);
        createUser("patient_elvin", "elvin123", "elvin@clinic.az", "Elvin Quliyev", "+994505555555", Role.PATIENT);
    }

    private void createUser(String username, String password, String email,
                            String fullName, String phoneNumber, Role role) {

        if (userRepository.existsByUsername(username)) {
            log.info("İstifadəçi artıq mövcuddur, keçilir: {}", username);
            return;
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .fullName(fullName)
                .phoneNumber(phoneNumber)
                .role(role)
                .build();

        userRepository.save(user);
        log.info("Test istifadəçisi yaradıldı: {}", username);
    }
}