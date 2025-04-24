package com.kunmi.taskManager.taskManager.dto;

import java.util.List;

public record ProjectListResponseDTO(
        List<ProjectDTO> projects,
        String message
) {
    public ProjectListResponseDTO(String message) {
        this(List.of(), message);
    }
}
