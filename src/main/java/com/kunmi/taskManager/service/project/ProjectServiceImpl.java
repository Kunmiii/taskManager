package com.kunmi.taskManager.service.project;

import com.kunmi.taskManager.repository.projectRepo.ProjectRepository;
import com.kunmi.taskManager.service.user.User;
import com.kunmi.taskManager.service.user.UserContext;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    private boolean isUserLoggedIn() {
        User loggedInUser = UserContext.getCurrentUser();

        if (loggedInUser == null) {
            System.out.println("No user is logged in");
            return false;
        }
        return true;
    }

    @Override
    public void create(String projectName, LocalDateTime createDate) {
        if (!isUserLoggedIn()) return;
        User loggedInUser = UserContext.getCurrentUser();

        Project project = new Project(projectName, createDate, loggedInUser.getId());
        projectRepository.addProject(loggedInUser.getId(), project);
        System.out.println("Project created successfully");
    }

    @Override
    public void update(String projectId, String newProjectName) {
        if (!isUserLoggedIn()) return;

        User loggedInUser = UserContext.getCurrentUser();
        Project retrievedProject = projectRepository.getProject(projectId, loggedInUser.getId());

        if (retrievedProject != null) {
            retrievedProject.setName(newProjectName);
            retrievedProject.setCreateDate(LocalDateTime.now());
            projectRepository.addProject(loggedInUser.getId(), retrievedProject);
            System.out.println("Project updated successfully!");
        } else {
            System.out.println("Project not found.");
        }
    }

    @Override
    public void view(String projectId) {
        if (!isUserLoggedIn()) return;

        User loggedInUser = UserContext.getCurrentUser();
        try {
            List<Project> userProjects = projectRepository.getUserProjects(loggedInUser.getId(), projectId);

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

        User loggedInUser = UserContext.getCurrentUser();
        try {
            Project response = projectRepository.getProject(projectId, loggedInUser.getId());

            if (response == null) {
                System.out.println("Project not found!");
                return;
            }

            projectRepository.removeProject(projectId, response.getId());
            System.out.println("project deleted successfully!");
        } catch (NullPointerException e) {
            System.out.println("Error: User not logged in or invalid project ID.");
        } catch (Exception e) {
            System.out.println("Unexpected error occurred while deleting the project: " + e.getMessage());
        }
    }
}
