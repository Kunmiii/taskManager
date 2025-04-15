package com.kunmi.taskManager.taskManager.records;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequestRecord(
        @NotBlank(message = "Project name cannot be blank")
        String projectName) {
}
