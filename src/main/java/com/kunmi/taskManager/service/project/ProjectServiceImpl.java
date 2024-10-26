package com.kunmi.taskManager.service.project;

import com.kunmi.taskManager.repository.projectRepo.ProjectRepository;
import com.kunmi.taskManager.service.user.UserImpl;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private UserImpl loggedInUserImpl;

    public ProjectServiceImpl(ProjectRepository projectRepository, UserImpl loggedInUserImpl) {
        this.projectRepository = projectRepository;
        this.loggedInUserImpl = loggedInUserImpl;
    }

    @Override
    public void setLoggedInUser(UserImpl loggedInUserImpl) {
        this.loggedInUserImpl = loggedInUserImpl;
    }

    private boolean isUserLoggedIn() {
        if (loggedInUserImpl == null) {
            System.out.println("No user is logged in");
            return true;
        }
        return false;
    }

    @Override
    public void create(String projectId, String projectName, LocalDateTime createDate) {
        if (isUserLoggedIn()) return;

        Project project = new Project(projectId, projectName, createDate, loggedInUserImpl.getId());
        projectRepository.saveProject(project);
        System.out.println("Project created successfully");
    }


    @Override
    public void update(String projectId, String newProjectName) {
        if (isUserLoggedIn()) return;
        Project retrievedProject = projectRepository.getProject(projectId, loggedInUserImpl.getId());

        if (retrievedProject != null) {
            retrievedProject.setName(newProjectName);
            retrievedProject.setCreateDate(LocalDateTime.now());
            projectRepository.saveProject(retrievedProject);
            System.out.println("Project updated successfully!");
        } else {
            System.out.println("Project not found.");
        }
    }

    @Override
    public void view() {
        if (isUserLoggedIn()) return;
        List<Project> userProjects = projectRepository.getUserProjects(loggedInUserImpl.getId());

        if (userProjects.isEmpty()){
            System.out.println("User has no project!");
            return;
        }
        userProjects.forEach(project -> System.out.println(project.toString()));
    }

    @Override
    public void delete(String projectId) {
        if (isUserLoggedIn()) return;
        Project response = projectRepository.getProject(projectId, loggedInUserImpl.getId());

        if (response == null) {
            System.out.println("Project not found!");
            return;
        }
            projectRepository.removeProject(projectId, loggedInUserImpl.getId());
    }
}
