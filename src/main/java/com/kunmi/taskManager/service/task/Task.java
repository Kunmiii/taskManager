package com.kunmi.taskManager.service.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Setter
@Getter
@ToString
@AllArgsConstructor


public class Task {

    private static  final AtomicInteger taskIdCounter = new AtomicInteger(1);
    private String id;
    private String name;
    private LocalDateTime createDate;
    private String projectId;

    public Task(String name, LocalDateTime createDate, String projectId) {
        this.id = getNextTaskId();
        this.name = name;
        this.createDate = createDate;
        this.projectId = projectId;
    }

    private static String getNextTaskId() {
        return String.valueOf(taskIdCounter.getAndIncrement());
    }


}
