package com.kunmi.taskManager.taskManager.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class ProjectListResponseDTO {
    private List<ProjectDTO> projects;
    private String message;

    public ProjectListResponseDTO(List<ProjectDTO> projects) {
        this.projects = projects;
        this.message = null;
    }

    public ProjectListResponseDTO(String message) {
        this.message = message;
        this.projects = null;
    }
}
