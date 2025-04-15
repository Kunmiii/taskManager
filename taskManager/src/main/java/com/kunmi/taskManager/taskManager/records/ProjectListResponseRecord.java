package com.kunmi.taskManager.taskManager.records;

import java.util.List;

public record ProjectListResponseRecord(
        List<ProjectRecord> projects,
        String message
) {
    public ProjectListResponseRecord(String message) {
        this(List.of(), message);
    }
}
