package com.kunmi.taskManager.service.project;

import java.time.LocalDateTime;
import java.util.List;

public interface ProjectService {
    void create(String projectName, LocalDateTime createDate);
    void update(String projectId, String projectName);
    List<Project> findAll();
    void delete(String id);
}
