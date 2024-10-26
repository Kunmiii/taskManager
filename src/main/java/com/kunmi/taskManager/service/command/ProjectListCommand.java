package com.kunmi.taskManager.service.command;

import com.kunmi.taskManager.service.project.ProjectService;

public class ProjectListCommand implements Command {

    private final ProjectService projectService;

    public ProjectListCommand(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public String getName() {
        return "project";
    }

    @Override
    public void execute() {
        System.out.println("====================");
        System.out.println("Display Projects");
        System.out.println("====================");

        projectService.view();
    }
}
