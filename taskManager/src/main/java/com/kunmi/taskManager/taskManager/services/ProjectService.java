package com.kunmi.taskManager.taskManager.services;

import com.kunmi.taskManager.taskManager.DTOs.*;

import java.util.UUID;

public interface ProjectService {
    ProjectListResponseDTO findAll(String email);
    GetProjectResponseDTO find(String email, UUID id);
    ProjectResponseDTO create(ProjectRequestDTo projectRequestDTo, String email);
    UpdateProjectResponseDTO update(UpdateProjectRequestDTO updateProjectRequestDTO, String email);
    DeleteProjectResponseDTO delete(String email, UUID id);
}
