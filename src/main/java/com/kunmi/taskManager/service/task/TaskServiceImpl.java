package com.kunmi.taskManager.service.task;

import com.kunmi.taskManager.exceptions.ProjectNotFoundException;
import com.kunmi.taskManager.exceptions.TaskNotFoundException;
import com.kunmi.taskManager.repository.projectRepo.ProjectRepository;
import com.kunmi.taskManager.repository.taskRepo.TaskRepository;
import com.kunmi.taskManager.utils.validation.ValidationUtils;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


public class TaskServiceImpl implements TaskServices {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    public TaskServiceImpl(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public void create(String taskName, String projectId, LocalDateTime createDate) {
        try {

            ValidationUtils.validateInputs(taskName, "taskName");
            ValidationUtils.validateInputs(projectId, "projectId");
            ValidationUtils.validateNotNull(createDate, "createDate");

            boolean projectExists = projectRepository.existsById(projectId);
            ValidationUtils.validateProjectExists(projectExists, projectId);

            Task task = new Task(taskName, createDate, projectId);
            taskRepository.addTask(projectId, task);

            logger.info("Project ID: {} - Task Name: {} created successfully",projectId, taskName);

        } catch (NullPointerException e) {
            logger.error("error while creating task: {}", e.getMessage());
            throw e;
        } catch ( IllegalArgumentException e) {
            logger.error("Null value encountered: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred: {}", e.getMessage());
        }
    }

    @Override
    public void update(String projectId, String taskId, String taskName) {

        try {

            ValidationUtils.validateInputs(taskId, "taskId");
            ValidationUtils.validateInputs(projectId, "projectId");
            ValidationUtils.validateInputs(taskName, "taskName");

            boolean projectExist = projectRepository.existsById(projectId);
            ValidationUtils.validateProjectExists(projectExist, projectId);

            Task task = taskRepository.getTask(taskId, projectId);
            if (task == null) {
                throw new TaskNotFoundException("Task with ID " + taskId + " not found in active projects");
            }

            task.setName(taskName);
            task.setCreateDate(LocalDateTime.now());

            taskRepository.updateTask(task, projectId);
            logger.info("Task with id {} was updated successfully:", taskId);

        } catch (TaskNotFoundException | IllegalArgumentException e) {
            logger.error("Error: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred while updating task: {}", e.getMessage());
        }
    }

    @SneakyThrows
    @Override
    public List<Task> findAll(String projectId) {

        try {
            ValidationUtils.validateInputs(projectId, "projectId");

            boolean projectExist = projectRepository.existsById(projectId);
            ValidationUtils.validateProjectExists(projectExist, projectId);

            List<Task> projectTask = taskRepository.getProjectTasks(projectId);
            if (projectTask == null || projectTask.isEmpty()) {
                logger.info("No tasks found for the project ID: {}", projectId);
                return Collections.emptyList();
            }
            return projectTask;

        } catch (ProjectNotFoundException | IllegalArgumentException e) {
            logger.error("Error fetching tasks for project ID {}: {}", projectId, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An expected error occurred while fetching tasks: {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public void delete(String projectId, String taskId) {

        try {
            ValidationUtils.validateInputs(taskId, "taskId");
            boolean projectExist = projectRepository.existsById(projectId);
            ValidationUtils.validateProjectExists(projectExist, projectId);

            taskRepository.removeTask(taskId, projectId);
            logger.info("Task with ID {} is deleted successfully from project {}", taskId, projectId);

        } catch (ProjectNotFoundException e) {
            logger.error("An error occurred: {} ", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Error deleting task: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred while deleting tasks with ID{}: {}", taskId, e.getMessage());
        }
    }
}
