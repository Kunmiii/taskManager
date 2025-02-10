package com.kunmi.taskManager.service.project;

import com.kunmi.taskManager.models.Project;

import java.time.LocalDateTime;
import java.util.List;

public interface ProjectService {
    void create(String projectName, LocalDateTime createDate);
    void update(Long projectId, String projectName);
    List<Project> findAll();
    void delete(Long id);
}
