package com.kunmi.taskManager.repository.projectRepo;

import com.kunmi.taskManager.service.project.Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectRepositoryImpl implements ProjectRepository {

    private final Map<String, Map<String, Project>> projectRepo = new HashMap<>();

    @Override
    public void addProject(String userId, Project project) {
        projectRepo
                .computeIfAbsent(userId, k -> new HashMap<>())
                .put(project.getId(), project);
    }

    @Override
    public Project getProject(String projectId, String userId) {
        Map<String, Project> userProjects = projectRepo.get(userId);
        return (userProjects != null) ? userProjects.get(projectId) : null;
    }

    @Override
    public List<Project> getUserProjects(String userId, String projectId) {
        Map<String, Project> userProjects = projectRepo.get(userId);
        return (userProjects != null) ? new ArrayList<>(userProjects.values()) : null;
    }

    @Override
    public void removeProject(String projectId, String userId) {
        Map<String, Project> userProjects = projectRepo.remove(userId);
        if (userProjects != null) {
            userProjects.remove(projectId);
            System.out.println("Project removed successfully");

            if (userProjects.isEmpty()) {
                projectRepo.remove(userId);
            }
        }
    }

    @Override
    public void removeAllProjectsForUser(String userid) {
        projectRepo.remove(userid);
        System.out.println("All Projects have been removed successfully");
    }
}
