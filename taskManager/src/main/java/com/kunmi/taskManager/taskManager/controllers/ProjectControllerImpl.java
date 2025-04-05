package com.kunmi.taskManager.taskManager.controllers;

import com.kunmi.taskManager.taskManager.DTOs.*;
import com.kunmi.taskManager.taskManager.models.User;
import com.kunmi.taskManager.taskManager.DTOs.DeleteProjectResponseDTO;
import com.kunmi.taskManager.taskManager.services.ProjectService;
import com.kunmi.taskManager.taskManager.utilities.SessionManager;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectControllerImpl implements ProjectController {

    private final ProjectService projectService;
    private final SessionManager sessionManager;

    @Override
    @GetMapping("/list")
    public ResponseEntity<ProjectListResponseDTO> getUserProjects(HttpSession session) {
        User currentUser = sessionManager.getCurrentUser(session);
        log.info("Fetching projects for user: {}", currentUser.getEmail());
        ProjectListResponseDTO projects = projectService.findAll(currentUser.getEmail());
        return ResponseEntity.ok(projects);
    }

    @Override
    @PostMapping("/create")
    public ResponseEntity<ProjectResponseDTO> createProject(@Valid @RequestBody ProjectRequestDTo projectRequestDTo, HttpSession session) {
        User currentUser = sessionManager.getCurrentUser(session);
        log.info("Creating project for user: {}", currentUser.getEmail());
        ProjectResponseDTO responseDTO = projectService.create(projectRequestDTo, currentUser.getEmail());
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<GetProjectResponseDTO> getUserProject(@PathVariable UUID id, HttpSession session) {
        User currentUser = sessionManager.getCurrentUser(session);
        log.info("Fetching project {} for user: {}", id, currentUser.getEmail());
        GetProjectResponseDTO project = projectService.find(currentUser.getEmail(), id);
        return ResponseEntity.ok(project);
    }

    @Override
    @PutMapping("/update")
    public ResponseEntity<UpdateProjectResponseDTO> updateProject(@Valid @RequestBody UpdateProjectRequestDTO updateProjectRequestDTO, HttpSession session) {
        User currentUser = sessionManager.getCurrentUser(session);
        log.info("Updating project for user {}", currentUser.getEmail());
        UpdateProjectResponseDTO response = projectService.update(updateProjectRequestDTO, currentUser.getEmail());
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DeleteProjectResponseDTO> deleteProject(@PathVariable UUID id, HttpSession session) {
        User currentUser = sessionManager.getCurrentUser(session);
        DeleteProjectResponseDTO response = projectService.delete(currentUser.getEmail(), id);
        return ResponseEntity.ok(response);
    }
}
