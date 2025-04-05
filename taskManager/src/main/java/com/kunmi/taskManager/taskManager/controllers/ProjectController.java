package com.kunmi.taskManager.taskManager.controllers;

import com.kunmi.taskManager.taskManager.DTOs.*;
import com.kunmi.taskManager.taskManager.DTOs.DeleteProjectResponseDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ProjectController {
    ResponseEntity<ProjectListResponseDTO> getUserProjects(HttpSession session);
    ResponseEntity<ProjectResponseDTO> createProject(ProjectRequestDTo projectRequestDTo, HttpSession session);
    ResponseEntity<GetProjectResponseDTO> getUserProject(UUID id, HttpSession session);
    ResponseEntity<UpdateProjectResponseDTO> updateProject(UpdateProjectRequestDTO updateProjectRequestDTO, HttpSession session);
    ResponseEntity<DeleteProjectResponseDTO> deleteProject(UUID id, HttpSession session);
}
