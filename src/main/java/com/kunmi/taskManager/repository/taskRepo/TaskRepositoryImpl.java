package com.kunmi.taskManager.repository.taskRepo;

import com.kunmi.taskManager.service.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskRepositoryImpl implements TaskRepository {
    private final Map<String, Map<String, Task>> taskRepo = new HashMap<>();

    @Override
    public void addTask(String projectId, Task task) {
        taskRepo
                .computeIfAbsent(projectId, k -> new HashMap<>())
                .put(task.getId(), task);
    }

    @Override
    public Task getTask(String taskId, String projectId) {
        Map<String, Task> projectTask = taskRepo.get(projectId);
        return (projectTask != null) ? projectTask.get(taskId) : null;
    }

    @Override
    public List<Task> getProjectTasks(String projectId) {
         Map<String, Task> projectTask = taskRepo.get(projectId);
         return (projectTask != null) ? new ArrayList<>(projectTask.values()) : null;
    }

    @Override
    public void removeTask(String taskId, String projectId) {
        Map<String, Task> projectTask = taskRepo.get(projectId);

        if (projectTask != null) {
            projectTask.remove(taskId);
            System.out.println("Task remove successfully!");

            if (projectTask.isEmpty()) {
                taskRepo.remove(projectId);
            }
        }
    }

    @Override
    public void removeAllTask(String projectId) {
       taskRepo.remove(projectId);
        System.out.println("All tasks have been removed!");
    }
}
