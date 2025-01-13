package com.kunmi.taskManager.repository.taskRepo;

import com.kunmi.taskManager.service.task.Task;

import java.util.List;

public interface TaskRepository {
    void addTask(String taskId, Task task);
    Task getTask(String taskId, String projectId);
    List<Task> getProjectTasks(String projectId);
    void removeTask(String taskId, String projectId);
    void removeAllTask(String projectId);
    void updateTask(Task task, String projectId);
}
