package com.kunmi.taskManager.service.project;

import com.kunmi.taskManager.repository.projectRepo.ProjectRepository;
import com.kunmi.taskManager.service.user.UserImpl;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private UserImpl loggedInUserImpl;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public UserImpl getLoggedInUser() {
        return loggedInUserImpl;
    }

    @Override
    public void setLoggedInUser(UserImpl loggedInUserImpl) {
        this.loggedInUserImpl = loggedInUserImpl;
    }

    private boolean isUserLoggedIn() {
        if (loggedInUserImpl == null) {
            System.out.println("No user is logged in");
            return false;
        }
        return true;
    }

    @Override
    public void create(String projectName, LocalDateTime createDate) {
        if (!isUserLoggedIn()) return;

        Project project = new Project(projectName, createDate, loggedInUserImpl.getId());
        projectRepository.addProject(loggedInUserImpl.getId(), project);
        System.out.println("Project created successfully");
    }

    @Override
    public void update(String projectId, String newProjectName) {
        if (!isUserLoggedIn()) return;
        Project retrievedProject = projectRepository.getProject(projectId, loggedInUserImpl.getId());

        if (retrievedProject != null) {
            retrievedProject.setName(newProjectName);
            retrievedProject.setCreateDate(LocalDateTime.now());
            projectRepository.addProject(loggedInUserImpl.getId(), retrievedProject);
            System.out.println("Project updated successfully!");
        } else {
            System.out.println("Project not found.");
        }
    }

    @Override
    public void view(String projectId) {
        if (!isUserLoggedIn()) return;
        try {
            List<Project> userProjects = projectRepository.getUserProjects(loggedInUserImpl.getId(), projectId);

            if (userProjects.isEmpty()) {
                System.out.println("User has no project!");
                return;
            }
            userProjects.forEach(project -> System.out.println(project.toString()));
        } catch (NullPointerException e) {
            System.out.println("Error: Unable to retrieve projects. User might not be logged in or Project ID maybe invalid");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while viewing projects: "+ e.getMessage());
        }
    }

    @Override
    public void delete(String projectId) {
        if (!isUserLoggedIn()) return;
        try {
            Project response = projectRepository.getProject(projectId, loggedInUserImpl.getId());

            if (response == null) {
                System.out.println("Project not found!");
                return;
            }

            projectRepository.removeProject(projectId, loggedInUserImpl.getId());
            System.out.println("project deleted successfully!");
        } catch (NullPointerException e) {
            System.out.println("Error: User not logged in or invalid project ID.");
        } catch (Exception e) {
            System.out.println("Unexpected error occurred while deleting the project: " + e.getMessage());
        }
    }
}
