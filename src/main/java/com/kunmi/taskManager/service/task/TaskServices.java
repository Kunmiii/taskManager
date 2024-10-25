package com.kunmi.taskManager.service.task;

public interface TaskServices {
    Task create(Task task);
    Task update(Task task);
    void view(int taskID);
    void delete(int taskID);
}
