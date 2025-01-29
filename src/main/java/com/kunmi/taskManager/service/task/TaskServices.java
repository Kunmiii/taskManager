package com.kunmi.taskManager.service.task;

import com.kunmi.taskManager.models.Task;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskServices {
    void create(String taskName, Long projectId, LocalDateTime createDate);
    void update(Long projectId, Long taskId, String taskName);
    List<Task> findAll(Long projectId);
    void delete(Long projectId, Long taskId);
}
