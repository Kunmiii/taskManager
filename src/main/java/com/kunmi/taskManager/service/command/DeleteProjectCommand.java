package com.kunmi.taskManager.service.command;

import com.kunmi.taskManager.utils.input.ScannerUtil;
import com.kunmi.taskManager.service.project.ProjectService;

public class DeleteProjectCommand implements Command {

    private final ProjectService projectService;

    public DeleteProjectCommand(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public String getName() {
        return "delete project";
    }

    @Override
    public void execute() {
        System.out.println("====================");
        System.out.println("Display Projects");
        System.out.println("====================");

        String projectId = ScannerUtil.getString("Enter project id: ");
        projectService.delete(projectId);

    }
}
