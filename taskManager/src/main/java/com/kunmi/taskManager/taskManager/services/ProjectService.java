package com.kunmi.taskManager.taskManager.services;

import com.kunmi.taskManager.taskManager.records.*;

import java.util.UUID;

public interface ProjectService {
    ProjectListResponseRecord findAll(String email);
    GetProjectResponseRecord find(String email, UUID id);
    ProjectResponseRecord create(ProjectRequestRecord projectRequestRecord, String email);
    UpdateProjectResponseRecord update(UUID id, UpdateProjectRequestRecord updateProjectRequestRecord, String email);
    DeleteProjectResponseRecord delete(String email, UUID id);
}
