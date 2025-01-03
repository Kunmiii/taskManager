package com.kunmi.taskManager.service.command;

import com.kunmi.taskManager.repository.commandRepo.CommandRepository;
import com.kunmi.taskManager.service.project.ProjectService;
import com.kunmi.taskManager.service.task.TaskServices;
import com.kunmi.taskManager.service.user.UserService;

public class CommandServiceImpl implements CommandService {

    private final CommandRepository commandPersistence;

    public CommandServiceImpl(UserService userService, ProjectService projectService, CommandRepository commandPersistence, TaskServices taskServices) {
        this.commandPersistence = commandPersistence;
        registerCommand(new HelpCommand());
        registerCommand(new RegistrationCommand(userService));
        registerCommand(new LoginCommand(userService));
        registerCommand(new CreateProjectCommand(projectService));
        registerCommand(new ProjectListCommand(projectService));
        registerCommand(new UpdateProjectCommand(projectService));
        registerCommand(new DeleteProjectCommand(projectService));
        registerCommand(new CreateTaskCommand(taskServices));
        registerCommand(new TaskListCommand(taskServices));
        registerCommand(new UpdateTaskCommand(taskServices));
        registerCommand(new DeleteTaskCommand(taskServices));
    }

    public void registerCommand(Command command) {
            commandPersistence.saveCommand(command);
    }

    public void executeCommand(String commandName) {
        Command command = commandPersistence.getCommand(commandName);
        if (command != null) {
            command.execute();
        } else {
            System.out.println("Invalid command:" + commandName);
        }
    }

}
