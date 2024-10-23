package com.kunmi.taskManager.repository;

import com.kunmi.taskManager.project.Project;

import java.util.List;

public interface ProjectRepository {
    void saveProject(Project project);
    Project getProject(String projectName, String userId);
    List<Project> getUserProjects(String userId);
    void removeProject(String projectName, String userID);
}
