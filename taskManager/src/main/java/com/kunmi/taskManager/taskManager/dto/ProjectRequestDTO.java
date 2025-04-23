package com.kunmi.taskManager.taskManager.dto;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequestDTO(
        @NotBlank(message = "Project name cannot be blank")
        String projectName) {
}
