package com.kunmi.taskManager.taskManager.records;

import java.time.LocalDateTime;
import java.util.UUID;

public record RegisterUserResponseRecord(UUID id, String firstName,
                                         String lastName, String email,
                                         LocalDateTime createdAt) {

}
