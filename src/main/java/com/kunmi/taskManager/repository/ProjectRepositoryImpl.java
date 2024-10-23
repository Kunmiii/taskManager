package com.kunmi.taskManager.repository;

import com.kunmi.taskManager.project.Project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectRepositoryImpl implements ProjectRepository {

    private final Map<String, Project> projectList = new HashMap<>();

    @Override
    public void saveProject(Project project) {
        projectList.put(project.getId(), project);
        System.out.println("Project saved successfully.");
    }

    @Override
    public Project getProject(String projectId, String userId) {
        Project project = projectList.get(projectId);
        if (project != null && project.getUserId().equalsIgnoreCase(userId)) {
            return project;
        }
        return null;
    }

    @Override
    public List<Project> getUserProjects(String userId) {
        return projectList.values().stream()
                .filter(project -> project.getUserId().equalsIgnoreCase(userId))
                .collect(Collectors.toList());
    }

    @Override
    public void removeProject(String projectId, String userId) {
        Project removedProject = projectList.remove(projectId);
        if (removedProject != null && removedProject.getUserId().equalsIgnoreCase(userId)) {
            System.out.println("Project removed successfully");
        } else {
            System.out.println("Project not found.");
        }
    }
}
