package com.kunmi.taskManager.command;

import com.kunmi.taskManager.service.CommandService;

public class HelpCommand implements CommandService {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void execute() {
        System.out.println("====================");
        System.out.println("Help Command List");
        System.out.println("====================");
        System.out.println("/help: List all commands with descriptions.");
        System.out.println("/registration: Register a new user.");
        System.out.println("/auth: Log in to the system.");
        System.out.println("/projects: List all projects.");
        System.out.println("/create_project: Create a new project.");
        System.out.println("/update_project: Rename a project.");
        System.out.println("/delete_project: Delete a project");
        System.out.println("/tasks: List all tasks in a project");
        System.out.println("/create_task: Create a task");
        System.out.println("/update_task: Rename a task.");
        System.out.println("/delete_task: Delete a task.");
        System.out.println("/exit: Log out. ");
    }
}
