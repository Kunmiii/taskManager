package com.kunmi.taskManager.service.project;

import java.time.LocalDateTime;

public interface ProjectService {
    void create(String projectId, String projectName, LocalDateTime createDate);
    void update(String projectId);
    void view();
    void delete(String id);

}
