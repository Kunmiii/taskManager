package com.kunmi.taskManager.task;

public interface TaskServices {
    Task create(Task task);
    Task update(Task task);
    void view(Task task);
    void delete(Task task);
}
