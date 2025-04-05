package com.kunmi.taskManager.taskManager.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateProjectRequestDTO {

    @NotNull(message = "Project ID cannot be blank")
    private UUID id;
    @NotBlank(message = "Project name cannot be blank")
    private String projectName;

    public UpdateProjectRequestDTO(UUID id, String projectName) {
        this.id = id;
        this.projectName = projectName;
    }
}
