package com.kunmi.taskManager.service.command;

import com.kunmi.taskManager.models.Task;
import com.kunmi.taskManager.service.task.TaskServices;
import com.kunmi.taskManager.utils.input.ScannerUtil;

import java.util.List;

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

        String projectId = ScannerUtil.getString("Enter project ID: ");

        List<Task> result = taskServices.findAll(Long.valueOf(projectId));

        result.forEach(System.out::println);
    }
}
