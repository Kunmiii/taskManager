package com.kunmi.taskManager.repository.taskRepo;

import com.kunmi.taskManager.models.Task;

import java.util.List;

public interface TaskRepository {
    void addTask(Task task);
    Task getTask(Long taskId, Long projectId);
    List<Task> getProjectTasks(Long projectId);
    void removeTask(Long taskId, Long projectId);
    void removeAllTask(Long projectId);
    void updateTask(Task task);
}
