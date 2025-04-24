package com.kunmi.taskManager.taskManager.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record RegisterUserResponseDTO(UUID id, String firstName,
                                      String lastName, String email,
                                      LocalDateTime createdAt) {

}
