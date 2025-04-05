package com.kunmi.taskManager.taskManager.services;

import com.kunmi.taskManager.taskManager.DTOs.*;
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
        List<Project> projects = projectRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ProjectNotFoundException("No project found"));
        log.info("Found {} projects for user {}", projects.size(), user.getEmail());

        if (projects.isEmpty()) {
            log.info("No projects found for user: {}", user.getEmail());
            return new ProjectListResponseDTO("No projects found for user: " + user.getEmail());
        }

        List<ProjectDTO> projectDTOs = projects.stream()
                .map(project -> {
                    ProjectDTO dto = new ProjectDTO();
                    dto.setId(project.getId().toString());
                    dto.setName(project.getName());
                    return dto;
                }).toList();

        return new ProjectListResponseDTO(projectDTOs);
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
    public ProjectResponseDTO create(ProjectRequestDTo projectRequestDTo, String email) {
        User user = getCurrentUser(email);

        Project project = new Project(projectRequestDTo.getProjectName());
        project.setUser(user);
        projectRepository.save(project);

        log.info("Project: {} created for user {}", project.getName(), email);
        return new ProjectResponseDTO(project.getId(), "Project " + projectRequestDTo.getProjectName() + " created successfully");
    }

    @Override
    @Transactional
    public UpdateProjectResponseDTO update(UpdateProjectRequestDTO updateProjectRequestDTO, String email) {
        User currentUser = getCurrentUser(email);
        Project project = projectRepository.findById(updateProjectRequestDTO.getId())
                .orElseThrow(() -> new ProjectNotFoundException("Project not found for: " + currentUser.getEmail()));

        if (!project.getUser().getId().equals(currentUser.getId())) {
            throw new ProjectNotFoundException("Project not found for user: " + currentUser.getEmail());
        }

        project.setName(updateProjectRequestDTO.getProjectName());
        projectRepository.save(project);
        log.info("Project with ID {} updated successfully", project.getId());
        return new UpdateProjectResponseDTO("Project updated successfully");
    }

    @Override
    @Transactional
    public DeleteProjectResponseDTO delete(String email, UUID id) {
        User currentUser = getCurrentUser(email);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found for user: " + currentUser.getEmail()));

        if (!project.getUser().getId().equals(currentUser.getId())) {
            throw new ProjectNotFoundException(currentUser.getEmail() + " Unauthorized to delete this project");
        }

        projectRepository.deleteById(project.getId());
        log.info("Project ID {} is successfully deleted", project.getId());
        return new DeleteProjectResponseDTO("Project deleted successfully");
    }

    private User getCurrentUser(String email) {
        return userRepository.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
    }
}
