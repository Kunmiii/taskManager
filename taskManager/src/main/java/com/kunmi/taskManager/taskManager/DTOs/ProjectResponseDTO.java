package com.kunmi.taskManager.taskManager.DTOs;

import com.kunmi.taskManager.taskManager.models.Project;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ProjectResponseDTO {

    private Project project;
    @NotNull(message = "Project ID cannot be blank")
    private UUID projectId;

    @NotBlank(message = "Message cannot be blank")
    private String message;

    public ProjectResponseDTO(UUID projectId, String message) {
        this.projectId = projectId;
        this.message = message;
    }

    public ProjectResponseDTO(Project project) {
        this.project = project;
    }
}
