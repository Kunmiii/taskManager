package com.kunmi.taskManager.service.command;

import com.kunmi.taskManager.service.project.Project;
import com.kunmi.taskManager.service.project.ProjectService;

import java.util.List;

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

        List<Project> projectList = projectService.findAll();
        projectList.forEach(System.out::println);

    }
}
