package com.kunmi.taskManager.taskManager.repositories;

import com.kunmi.taskManager.taskManager.models.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> getUserByEmail(@NotBlank(message = "Password cannot be blank") String password);
}
