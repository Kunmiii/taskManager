package com.kunmi.taskManager.taskManager.DTOs;

import lombok.Data;

@Data
public class DeleteProjectResponseDTO {

    private String message;

    public DeleteProjectResponseDTO(String message) {
        this.message = message;
    }
}
