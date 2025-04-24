package com.kunmi.taskManager.taskManager.dto;

import java.util.UUID;

public record GetProjectResponseDTO(
        UUID id, String projectName
) {
}
