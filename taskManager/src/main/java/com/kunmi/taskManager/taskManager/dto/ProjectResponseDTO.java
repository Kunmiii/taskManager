package com.kunmi.taskManager.taskManager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProjectResponseDTO(
        @NotNull(message = "Project ID cannot be null")
        UUID id,

        @NotBlank(message = "Project name cannot be blank")
        String name,

        @NotBlank(message = "Message cannot be blank")
        String message
) {
    public ProjectResponseDTO(UUID id, String name) {
        this(id, name, "Project created successfully");
    }

    public ProjectResponseDTO(String message) {
        this(null,null, message);
    }
}
