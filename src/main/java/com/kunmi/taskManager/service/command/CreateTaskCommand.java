package com.kunmi.taskManager.service.command;

import com.kunmi.taskManager.input.scannerUtil.ScannerUtil;
import com.kunmi.taskManager.service.task.TaskServices;

import java.time.LocalDateTime;

public class CreateTaskCommand implements Command {
    private final TaskServices taskServices;

    public CreateTaskCommand(TaskServices taskServices) {
        this.taskServices = taskServices;
    }

    @Override
    public String getName() {
        return "create task";
    }

    @Override
    public void execute() {
        System.out.println("====================");
        System.out.println("Create New Task");
        System.out.println("====================");

        String taskName = ScannerUtil.getString("Enter Task name: ");
        String projectId = ScannerUtil.getString(" Enter Project id: ");
        LocalDateTime createDate = LocalDateTime.now();

        taskServices.create(projectId, taskName, createDate);
    }
}
