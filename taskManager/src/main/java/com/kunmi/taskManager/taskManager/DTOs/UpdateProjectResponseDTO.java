package com.kunmi.taskManager.taskManager.DTOs;

import lombok.Data;

@Data
public class UpdateProjectResponseDTO {

    private String message;

    public UpdateProjectResponseDTO(String message) {
        this.message = message;
    }
}
