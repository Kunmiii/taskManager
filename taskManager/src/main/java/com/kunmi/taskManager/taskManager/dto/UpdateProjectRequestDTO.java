package com.kunmi.taskManager.taskManager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateProjectRequestDTO(
        @NotNull(message = "Project ID cannot be blank")
        UUID id,
        @NotBlank(message = "Project name cannot be blank")
        String projectName

) {
}
