package com.kunmi.taskManager.service.command;

import com.kunmi.taskManager.utils.input.ScannerUtil;
import com.kunmi.taskManager.service.task.Task;
import com.kunmi.taskManager.service.task.TaskServices;

import java.util.List;
import java.util.stream.Collectors;

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

        List<Task> result = taskServices.findAll(projectId);
        result.stream()
                .collect(Collectors.toList())
                .forEach(System.out::println);
    }
}
