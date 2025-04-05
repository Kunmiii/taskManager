package com.kunmi.taskManager.taskManager.DTOs;

import lombok.Data;

import java.util.UUID;

@Data
public class GetProjectResponseDTO {

    private UUID id;
    private String projectName;

    public GetProjectResponseDTO(UUID id, String projectName) {
        this.id = id;
        this.projectName = projectName;
    }
}
