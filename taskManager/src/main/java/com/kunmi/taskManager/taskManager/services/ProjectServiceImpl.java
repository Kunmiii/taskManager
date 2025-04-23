package com.kunmi.taskManager.taskManager.services;

import com.kunmi.taskManager.taskManager.dto.*;
import com.kunmi.taskManager.taskManager.exceptions.ProjectNotFoundException;
import com.kunmi.taskManager.taskManager.exceptions.UserNotFoundException;
import com.kunmi.taskManager.taskManager.models.Project;
import com.kunmi.taskManager.taskManager.models.User;
import com.kunmi.taskManager.taskManager.repositories.ProjectRepository;
import com.kunmi.taskManager.taskManager.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Override
    public ProjectListResponseDTO findAll(String email) {
        User user = getCurrentUser(email);

        log.info("Fetching projects for user: {}", user.getEmail());
        List<Project> projects = projectRepository.findByUserId(user.getId());
        log.info("Found {} projects for user {}", projects.size(), user.getEmail());

        if (projects.isEmpty()) {
            log.info("No projects found for user: {}", user.getEmail());
            return new ProjectListResponseDTO("No projects found for user: " + user.getEmail());
        }

        List<ProjectDTO> projectDTOS = projects.stream()
                .map(project -> new ProjectDTO(project.getId().toString(), project.getName())
                ).toList();

        return new ProjectListResponseDTO(projectDTOS, null);
    }

    @Override
    public GetProjectResponseDTO find(String email, UUID id) {
        User currentUser = getCurrentUser(email);
        Project project = projectRepository.findById(id).orElseThrow(() ->
                new ProjectNotFoundException("Project not found for user: " + currentUser.getEmail()));

        if (!project.getUser().getId().equals(currentUser.getId())) {
            throw new ProjectNotFoundException("Project not found for user:" + currentUser.getEmail());
        }

        return new GetProjectResponseDTO(project.getId(), project.getName());
    }

    @Override
    @Transactional
    public ProjectResponseDTO create(ProjectRequestDTO projectRequestDTO, String email) {
        User user = getCurrentUser(email);

        Project project = new Project(projectRequestDTO.projectName());
        project.setUser(user);
        projectRepository.save(project);

        log.info("Project: {} created for user {}", project.getName(), email);
        return new ProjectResponseDTO(project.getId(), project.getName());
    }

    @Override
    @Transactional
    public void update(UUID id, UpdateProjectRequestDTO updateProjectRequestDTO, String email) {
        User currentUser = getCurrentUser(email);
        Project project = projectRepository.findById(updateProjectRequestDTO.id())
                .orElseThrow(() -> new ProjectNotFoundException("Project not found for: " + currentUser.getEmail()));

        if (!project.getId().equals(currentUser.getId())) {
            throw new ProjectNotFoundException("Project not found for user: " + currentUser.getEmail());
        }

        project.setName(updateProjectRequestDTO.projectName());
        projectRepository.save(project);
        log.info("Project with ID {} updated successfully", project.getId());
    }

    @Override
    @Transactional
    public void delete(String email, UUID id) {
        User currentUser = getCurrentUser(email);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found for user: " + currentUser.getEmail()));

        if (!project.getUser().getId().equals(currentUser.getId())) {
            throw new ProjectNotFoundException(currentUser.getEmail() + " Unauthorized to delete this project");
        }

        projectRepository.deleteById(project.getId());
        log.info("Project ID {} is successfully deleted", project.getId());
    }

    private User getCurrentUser(String email) {
        return userRepository.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
    }
}
