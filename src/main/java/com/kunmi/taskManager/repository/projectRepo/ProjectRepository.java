package com.kunmi.taskManager.repository.projectRepo;

import com.kunmi.taskManager.service.project.Project;

import java.util.List;

public interface ProjectRepository {
    void addProject(String userId, Project project);
    Project getProject(String projectId, String userId);
    List<Project> getUserProjects(String userId, String projectId);
    void removeProject(String projectName, String userID);
    void removeAllProjectsForUser(String userid);
}
