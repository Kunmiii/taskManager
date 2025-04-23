package com.kunmi.taskManager.taskManager.controllers;

import com.kunmi.taskManager.taskManager.dto.*;
import com.kunmi.taskManager.taskManager.models.User;
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
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class ProjectControllerImpl {

    private final ProjectService projectService;
    private final SessionManager sessionManager;

    @GetMapping("/projects")
    public ResponseEntity<ProjectListResponseDTO> getUserProjects(HttpSession session) {
        User currentUser = sessionManager.getCurrentUser(session);
        log.info("Fetching projects for user: {}", currentUser.getEmail());
        ProjectListResponseDTO projects = projectService.findAll(currentUser.getEmail());
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/projects")
    public ResponseEntity<ProjectResponseDTO> createProject(@Valid @RequestBody ProjectRequestDTO projectRequestDTO, HttpSession session) {
        User currentUser = sessionManager.getCurrentUser(session);
        log.info("Creating project for user: {}", currentUser.getEmail());
        ProjectResponseDTO responseDTO = projectService.create(projectRequestDTO, currentUser.getEmail());
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("projects/{id}")
    public ResponseEntity<GetProjectResponseDTO> getUserProject(@PathVariable UUID id, HttpSession session) {
        User currentUser = sessionManager.getCurrentUser(session);
        log.info("Fetching project {} for user: {}", id, currentUser.getEmail());
        GetProjectResponseDTO project = projectService.find(currentUser.getEmail(), id);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<Void> updateProject(@PathVariable UUID id, @Valid @RequestBody UpdateProjectRequestDTO updateProjectRequestDTO, HttpSession session) {
        User currentUser = sessionManager.getCurrentUser(session);
        log.info("Updating project for user {}", currentUser.getEmail());
        projectService.update(id, updateProjectRequestDTO, currentUser.getEmail());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id, HttpSession session) {
        User currentUser = sessionManager.getCurrentUser(session);
        projectService.delete(currentUser.getEmail(), id);
        return ResponseEntity.noContent().build();
    }
}
