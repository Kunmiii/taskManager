package com.kunmi.taskManager.service.project;

import java.time.LocalDateTime;

public interface ProjectService {
    void create(String projectName, LocalDateTime createDate);
    void update(String projectId, String projectName);
    void view(String projectId);
    void delete(String id);
}
