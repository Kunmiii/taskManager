package com.kunmi.taskManager.service.task;

import java.time.LocalDateTime;

public interface TaskServices {
    void create(String taskName, LocalDateTime createDate);
    void update(String taskId, String taskName);
    void view();
    void delete(String taskId);
}
