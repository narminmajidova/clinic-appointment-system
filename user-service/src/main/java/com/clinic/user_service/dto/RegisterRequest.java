package com.clinic.user_service.dto;

import com.clinic.user_service.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Username boş ola bilməz")
    private String username;

    @NotBlank(message = "Password boş ola bilməz")
    private String password;

    @Email(message = "Email formatı düzgün deyil")
    @NotBlank(message = "Email boş ola bilməz")
    private String email;

    private String fullName;

    private String phoneNumber;

    private Role role;
}