package com.kunmi.taskManager.repository.projectRepo;

import com.kunmi.taskManager.service.project.Project;

import java.util.List;

public interface ProjectRepository {
    void saveProject(Project project);
    Project getProject(String projectId, String userId);
    List<Project> getUserProjects(String userId);
    void removeProject(String projectName, String userID);
}
