package com.kunmi.taskManager.service.command;

import com.kunmi.taskManager.service.task.TaskServices;

public class TaskListCommand implements Command {
    private final TaskServices taskServices;

    public TaskListCommand(TaskServices taskServices) {
        this.taskServices = taskServices;
    }

    @Override
    public String getName() {
        return "view task";
    }

    @Override
    public void execute() {
        System.out.println("====================");
        System.out.println("View All Tasks");
        System.out.println("====================");

        taskServices.view();
    }
}
