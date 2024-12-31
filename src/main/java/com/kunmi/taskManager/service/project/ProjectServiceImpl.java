package com.kunmi.taskManager.service.project;

import com.kunmi.taskManager.exceptions.ProjectNotFoundException;
import com.kunmi.taskManager.repository.projectRepo.ProjectRepository;
import com.kunmi.taskManager.service.user.User;
import com.kunmi.taskManager.service.user.UserContext;
import com.kunmi.taskManager.utils.validation.ValidationUtils;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public void create(String projectName, LocalDateTime createDate) {

        try {
            User loggedInUser = UserContext.getCurrentUser();
            ValidationUtils.validateNotNull(loggedInUser, "Logged-in user");
            ValidationUtils.validateInputs(projectName, "ProjectName");
            ValidationUtils.validateNotNull(createDate, "createDate");

            Project project = new Project(projectName, createDate, loggedInUser.getId());
            System.out.println("New project created");
            projectRepository.addProject(loggedInUser.getId(), project);

            System.out.println("Inside create method - addProject called");

            logger.info("Project ID: {}, Project Name: {}, User ID: {}", project.getId(), projectName, loggedInUser.getId());

        } catch (NullPointerException | IllegalArgumentException e) {
            logger.error("Error: {} ", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while creating project: {}", e.getMessage());
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
        }
    }

    //@SneakyThrows
    @SneakyThrows
    @Override
    public void update(String projectId, String newProjectName) {
        try {
            User loggedInUser = UserContext.getCurrentUser();
            ValidationUtils.validateNotNull(loggedInUser, "logged-in user");
            ValidationUtils.validateInputs(projectId, "fieldName");
            ValidationUtils.validateInputs(newProjectName, "NewProjectName");

            Project retrievedProject = projectRepository.getProject(projectId, loggedInUser.getId());
            ValidationUtils.validateUserProject(retrievedProject, loggedInUser.getId());

            retrievedProject.setName(newProjectName);
            retrievedProject.setCreateDate(LocalDateTime.now());

            projectRepository.updateProject(loggedInUser.getId(), retrievedProject);

            logger.info("Project updated successfully: ID = {}", projectId);
        } catch (NullPointerException e) {
            logger.error("Null value encountered - {} ", e.getMessage());
        } catch (ProjectNotFoundException | IllegalArgumentException e) {
            logger.error("Error- {} ", e.getMessage());
            throw e;
        } catch (RuntimeException e) {
            logger.error("An unexpected error occurred: {}", e.getMessage());
            throw new RuntimeException("An unexpected error occurred while fetching data: " + e.getMessage());
        }
    }

    @Override
    public List<Project> findAll() {
        try {
            User loggedInUser = UserContext.getCurrentUser();
            ValidationUtils.validateNotNull(loggedInUser, "Logged-in user");
            List<Project> userProjects = projectRepository.getUserProjects(loggedInUser.getId());

            ValidationUtils.validateUserProjects(userProjects, loggedInUser.getId());

            return userProjects;

        } catch (NullPointerException | ProjectNotFoundException e) {
            logger.error("Errors: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred while viewing projects: {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    @SneakyThrows
    @Override
    public void delete(String projectId) {
        try {
            User loggedInUser = UserContext.getCurrentUser();
            ValidationUtils.validateNotNull(loggedInUser, "Logged-in user");
            ValidationUtils.validateInputs(projectId, "ProjectId");

            Project response = projectRepository.getProject(projectId, loggedInUser.getId());
            ValidationUtils.validateUserProject(response, loggedInUser.getId());

            projectRepository.removeProject(projectId, loggedInUser.getId());
            logger.info("Project with ID {} deleted successfully for user {}", projectId, loggedInUser.getFirstName());

        } catch (NullPointerException | ProjectNotFoundException e) {
            logger.error("Error occurred: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error occurred: {}", e.getMessage());
        }
    }
}
