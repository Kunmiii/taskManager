package com.kunmi.taskManager.repository.projectRepo;

import com.kunmi.taskManager.exceptions.ProjectNotFoundException;
import com.kunmi.taskManager.service.project.Project;

import java.util.List;

public interface ProjectRepository {
    void addProject(String userId, Project project);
    Project getProject(String projectId, String userId) throws ProjectNotFoundException;
    List<Project> getUserProjects(String userId);
    void removeProject(String projectName, String userID);
    void removeAllProjectsForUser(String userid);
    boolean existsById(String projectId);
}
