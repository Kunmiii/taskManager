package com.kunmi.taskManager.service.command;

import com.kunmi.taskManager.input.scannerUtil.ScannerUtil;
import com.kunmi.taskManager.service.task.TaskServices;

public class UpdateTaskCommand implements Command {
    private final TaskServices taskServices;

    public UpdateTaskCommand(TaskServices taskServices) {
        this.taskServices = taskServices;
    }

    @Override
    public String getName() {
        return "update task";
    }

    @Override
    public void execute() {
        System.out.println("====================");
        System.out.println("Update Project Task");
        System.out.println("====================");

        String taskId = ScannerUtil.getString("Enter Task ID: ");
        String newTaskName = ScannerUtil.getString("Enter new task name: ");

        taskServices.update(taskId, newTaskName);
    }
}
