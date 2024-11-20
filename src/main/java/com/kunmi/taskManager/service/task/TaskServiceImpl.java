package com.kunmi.taskManager.service.task;

import com.kunmi.taskManager.repository.taskRepo.TaskRepository;
import com.kunmi.taskManager.service.project.Project;
import com.kunmi.taskManager.service.project.ProjectContext;

import java.time.LocalDateTime;
import java.util.List;

public class TaskServiceImpl implements TaskServices {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void create(String taskName, LocalDateTime createDate) {
        Project project = ProjectContext.getCurrentProject();
        Task task = new Task(taskName, createDate, project.getId());

        taskRepository.addTask(project.getId(), task);
        System.out.println("Task created successfully!");
    }

    @Override
    public void update(String taskId, String taskName) {
        Project project = ProjectContext.getCurrentProject();

        Task task = taskRepository.getTask(taskId, project.getId());

        if (task != null) {
            task.setName(taskName);
            task.setCreateDate(LocalDateTime.now());
            System.out.println("Task updated successfully!");

        } else {
            System.out.println("Task not found.");
        }

    }

    @Override
    public void view() {
        Project project = ProjectContext.getCurrentProject();
        List<Task> projectTask = taskRepository.getProjectTasks(project.getId());
        projectTask.forEach(task -> System.out.println(task.toString()));
    }

    @Override
    public void delete(String taskId) {
        Project project = ProjectContext.getCurrentProject();
        taskRepository.removeTask(taskId, project.getId());
    }
}
