package com.kunmi.taskManager.service.task;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskServices {
    void create(String taskName, String projectId, LocalDateTime createDate);
    void update(String projectId, String taskId, String taskName);
    List<Task> findAll(String projectId);
    void delete(String projectId, String taskId);
}
