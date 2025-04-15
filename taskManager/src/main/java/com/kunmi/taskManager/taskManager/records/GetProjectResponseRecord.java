package com.kunmi.taskManager.taskManager.records;

import java.util.UUID;

public record GetProjectResponseRecord(
        UUID id, String projectName
) {
}
