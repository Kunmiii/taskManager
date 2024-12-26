package com.kunmi.taskManager.service.project;

import lombok.*;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class Project {

    private static final AtomicInteger projectIdCounter = new AtomicInteger(1);
    private String id;
    private String name;
    private LocalDateTime createDate;
    private String userId;

    public Project(String name, LocalDateTime createDate, String userId) {
        this.id = getNextProjectId();
        this.name = name;
        this.createDate = createDate;
        this.userId = userId;
    }

    private static String getNextProjectId() {
        return String.valueOf(projectIdCounter.getAndIncrement());
    }
}
