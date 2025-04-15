package com.kunmi.taskManager.taskManager.controllers;

import com.kunmi.taskManager.taskManager.records.*;
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
    public ResponseEntity<ProjectListResponseRecord> getUserProjects(HttpSession session) {
        User currentUser = sessionManager.getCurrentUser(session);
        log.info("Fetching projects for user: {}", currentUser.getEmail());
        ProjectListResponseRecord projects = projectService.findAll(currentUser.getEmail());
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/projects")
    public ResponseEntity<ProjectResponseRecord> createProject(@Valid @RequestBody ProjectRequestRecord projectRequestRecord, HttpSession session) {
        User currentUser = sessionManager.getCurrentUser(session);
        log.info("Creating project for user: {}", currentUser.getEmail());
        ProjectResponseRecord responseDTO = projectService.create(projectRequestRecord, currentUser.getEmail());
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("projects/{id}")
    public ResponseEntity<GetProjectResponseRecord> getUserProject(@PathVariable UUID id, HttpSession session) {
        User currentUser = sessionManager.getCurrentUser(session);
        log.info("Fetching project {} for user: {}", id, currentUser.getEmail());
        GetProjectResponseRecord project = projectService.find(currentUser.getEmail(), id);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<UpdateProjectResponseRecord> updateProject(@PathVariable UUID id, @Valid @RequestBody UpdateProjectRequestRecord updateProjectRequestRecord, HttpSession session) {
        User currentUser = sessionManager.getCurrentUser(session);
        log.info("Updating project for user {}", currentUser.getEmail());
        UpdateProjectResponseRecord response = projectService.update(id, updateProjectRequestRecord, currentUser.getEmail());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<DeleteProjectResponseRecord> deleteProject(@PathVariable UUID id, HttpSession session) {
        User currentUser = sessionManager.getCurrentUser(session);
        DeleteProjectResponseRecord response = projectService.delete(currentUser.getEmail(), id);
        return ResponseEntity.ok(response);
    }
}
