package com.kunmi.taskManager.repository.projectRepo;

import com.kunmi.taskManager.models.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    Project getProject(Long projectId, Long userId);
    List<Project> getUserProjects(Long userId);
    void removeProject(Long projectId, Long userID);
    void removeAllProjectsForUser(Long userid);
    boolean existsById(Long projectId);
    void updateProject(Project project);
    void saveProject(Project project);
    Optional<Project> findById(Long projectId);
}
