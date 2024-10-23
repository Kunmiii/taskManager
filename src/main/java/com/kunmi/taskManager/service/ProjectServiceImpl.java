package com.kunmi.taskManager.service;

import com.kunmi.taskManager.repository.ProjectRepository;
import com.kunmi.taskManager.project.Project;
import com.kunmi.taskManager.scannerUtil.ScannerUtil;
import com.kunmi.taskManager.user.User;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final User loggedInUser;

    public ProjectServiceImpl(ProjectRepository projectRepository, User loggedInUser) {
        this.projectRepository = projectRepository;
        this.loggedInUser = loggedInUser;
    }

    @Override
    public void create(String projectId, String projectName, LocalDateTime createDate) {
        Project project = new Project(projectId, projectName, createDate, loggedInUser.getId());

        projectRepository.saveProject(project);
        System.out.println("Project created successfully");
    }

    @Override
    public void update(String projectId) {
        Project retrievedProject = projectRepository.getProject(projectId, loggedInUser.getId());

        if (retrievedProject != null) {
            String newProjectName = ScannerUtil.getString("Enter project name:");
            retrievedProject.setName(newProjectName);
            projectRepository.saveProject(retrievedProject);
        } else {
            System.out.println("Project not found.");
        }
    }

    @Override
    public void view() {
        List<Project> userProjects = projectRepository.getUserProjects(loggedInUser.getId());
        userProjects.forEach(project -> System.out.println(project.toString()));
    }

    @Override
    public void delete(String projectId) {
        projectRepository.removeProject(projectId, loggedInUser.getId());
    }
}
