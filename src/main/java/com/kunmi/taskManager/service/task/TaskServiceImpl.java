package com.kunmi.taskManager.service.task;

import com.kunmi.taskManager.exceptions.ProjectNotFoundException;
import com.kunmi.taskManager.exceptions.TaskNotFoundException;
import com.kunmi.taskManager.models.Project;
import com.kunmi.taskManager.models.Task;
import com.kunmi.taskManager.repository.projectRepo.ProjectRepository;
import com.kunmi.taskManager.repository.taskRepo.TaskRepository;
import com.kunmi.taskManager.utils.validation.ValidationUtils;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class TaskServiceImpl implements TaskServices {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    public TaskServiceImpl(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public void create(String taskName, Long projectId, LocalDateTime createDate) {
        try {

            ValidationUtils.validateInputs(taskName, "taskName");
            ValidationUtils.validateInputs(String.valueOf(projectId), "projectId");
            ValidationUtils.validateNotNull(createDate, "createDate");

            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new IllegalArgumentException("Project with ID " + projectId + " does not exist"));

            Task task = new Task(taskName, createDate, project);
            taskRepository.addTask(task);

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
    public void update(Long projectId, Long taskId, String taskName) {

        try {

            ValidationUtils.validateInputs(String.valueOf(taskId), "taskId");
            ValidationUtils.validateInputs(String.valueOf(projectId), "projectId");
            ValidationUtils.validateInputs(taskName, "taskName");

            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + projectId + " does not exist"));

            Optional<Task> taskOptional = taskRepository.getTask(taskId, projectId);
            Task task = taskOptional.orElseThrow(() ->
                    new TaskNotFoundException("Task with ID " + taskId + " not found in active projects"));

            task.setName(taskName);
            taskRepository.updateTask(task);
            logger.info("Task with id {} was updated successfully:", taskId);

        } catch (TaskNotFoundException | IllegalArgumentException e) {
            logger.error("Error updating task with ID {} in project {}: {}", taskId, projectId, e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred while updating task: {}", e.getMessage());
        }
    }

    @SneakyThrows
    @Override
    public List<Task> findAll(Long projectId) {

        try {
            ValidationUtils.validateInputs(String.valueOf(projectId), "projectId");

            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + projectId + " does not exist"));

            List<Task> projectTask = taskRepository.getProjectTasks(projectId);
            if (projectTask.isEmpty()) {
                logger.info("No tasks found for the project ID: {}", projectId);
                return Collections.emptyList();
            }

            return projectTask;

        } catch (ProjectNotFoundException | IllegalArgumentException e) {
            logger.error("Error fetching tasks for project ID {}: {}", projectId, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while fetching tasks: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch tasks", e);
        }
    }

    @Override
    public void delete(Long projectId, Long taskId) {

        try {
            ValidationUtils.validateInputs(String.valueOf(taskId), "taskId");

            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + projectId + " does not exist"));


            taskRepository.removeTask(taskId, projectId);
            logger.info("Task with ID {} is deleted successfully from project {}", taskId, projectId);

        } catch (ProjectNotFoundException | IllegalArgumentException e) {
            logger.error("Error deleting task with ID {} from project {}: {}", taskId, projectId, e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred while deleting task: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete task", e);
        }
    }
}
