package com.kunmi.taskManager.service.command;

import com.kunmi.taskManager.input.scannerUtil.ScannerUtil;
import com.kunmi.taskManager.service.project.ProjectService;

import java.time.LocalDateTime;

public class CreateProjectCommand implements Command {

    private final ProjectService projectService;

    public CreateProjectCommand(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public String getName() {
        return "create project";
    }

    @Override
    public void execute() {
        System.out.println("====================");
        System.out.println("Create New Project");
        System.out.println("====================");

        String projectName = ScannerUtil.getString("Enter project name: ");
        LocalDateTime projectDate = LocalDateTime.now();

        projectService.create(projectName, projectDate);
    }
}
