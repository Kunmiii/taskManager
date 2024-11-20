package com.kunmi.taskManager.service.command;

import com.kunmi.taskManager.input.scannerUtil.ScannerUtil;
import com.kunmi.taskManager.service.task.TaskServices;

public class DeleteTaskCommand implements Command {

    private final TaskServices taskServices;

    public DeleteTaskCommand(TaskServices taskServices) {
        this.taskServices = taskServices;
    }

    @Override
    public String getName() {
        return "delete task";
    }

    @Override
    public void execute() {
        System.out.println("====================");
        System.out.println("Delete Project Task");
        System.out.println("====================");

        String taskId = ScannerUtil.getString("Enter Task ID: ");
        taskServices.delete(taskId);
    }
}