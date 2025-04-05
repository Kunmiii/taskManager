package com.kunmi.taskManager.taskManager.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjectRequestDTo {

    @NotBlank(message = "Project name cannot be blank")
    private String projectName;
}
