package com.kunmi.taskManager.service.command;

import com.kunmi.taskManager.utils.input.ScannerUtil;
import com.kunmi.taskManager.service.project.ProjectService;

public class UpdateProjectCommand implements Command {

    private final ProjectService projectService;

    public UpdateProjectCommand(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public String getName() {
        return "update project";
    }

    @Override
    public void execute() {
        System.out.println("====================");
        System.out.println("Update Projects");
        System.out.println("====================");

        String ProjectId = ScannerUtil.getString("Enter project id: ");
        String newProjectName = ScannerUtil.getString("Enter new project name:");
        projectService.update(ProjectId, newProjectName);
    }
}
