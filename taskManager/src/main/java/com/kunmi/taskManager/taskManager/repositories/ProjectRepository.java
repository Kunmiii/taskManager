package com.kunmi.taskManager.taskManager.repositories;

import com.kunmi.taskManager.taskManager.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Optional<List<Project>> findByUserId(UUID id);
}
