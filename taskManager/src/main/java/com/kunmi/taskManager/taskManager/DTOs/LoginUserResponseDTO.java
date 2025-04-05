package com.kunmi.taskManager.taskManager.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class LoginUserResponseDTO {

    private UUID id;
    private String email;
    private String message;

    public LoginUserResponseDTO(UUID id, String email, String message) {
        this.id = id;
        this.email = email;
        this.message = message;
    }
}
