package com.kunmi.taskManager.taskManager.DTOs;

import lombok.Data;

@Data
public class UserLoginResponseDTO {

    private String message;

    public UserLoginResponseDTO(String message) {
        this.message = message;
    }
}
