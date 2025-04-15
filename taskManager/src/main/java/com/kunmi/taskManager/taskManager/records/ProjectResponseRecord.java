package com.kunmi.taskManager.taskManager.records;

import com.kunmi.taskManager.taskManager.models.Project;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProjectResponseRecord(
        @NotNull(message = "Project ID cannot be null")
        UUID id,

        @NotBlank(message = "Project name cannot be blank")
        String name,

        @NotBlank(message = "Message cannot be blank")
        String message
) {
    public ProjectResponseRecord(UUID id, String name) {
        this(id, name, "Project created successfully");
    }

    public ProjectResponseRecord(String message) {
        this(null,null, message);
    }
}
