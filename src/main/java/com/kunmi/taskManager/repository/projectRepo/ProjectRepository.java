package com.kunmi.taskManager.repository.projectRepo;

import com.kunmi.taskManager.service.project.Project;

import java.util.List;

public interface ProjectRepository {
    Project getProject(String projectId, String userId);
    List<Project> getUserProjects(String userId);
    void removeProject(String projectName, String userID);
    void removeAllProjectsForUser(String userid);
    boolean existsById(String projectId);
    void updateProject(String projectId, Project project);
    void saveProject(String userId, Project project);
}
