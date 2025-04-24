package com.kunmi.taskManager.taskManager.services;

import com.kunmi.taskManager.taskManager.dto.*;

import java.util.UUID;

public interface ProjectService {
    ProjectListResponseDTO findAll(String email);
    GetProjectResponseDTO find(String email, UUID id);
    ProjectResponseDTO create(ProjectRequestDTO projectRequestDTO, String email);
    void update(UUID id, UpdateProjectRequestDTO updateProjectRequestDTO, String email);
    void delete(String email, UUID id);
}
