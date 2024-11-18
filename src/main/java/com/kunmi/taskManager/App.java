package com.kunmi.taskManager;

import com.kunmi.taskManager.input.scannerUtil.ScannerUtil;
import com.kunmi.taskManager.repository.commandRepo.CommandRepository;
import com.kunmi.taskManager.repository.commandRepo.CommandRepositoryImpl;
import com.kunmi.taskManager.repository.projectRepo.ProjectRepository;
import com.kunmi.taskManager.repository.projectRepo.ProjectRepositoryImpl;
import com.kunmi.taskManager.repository.userRepo.IUserRepository;
import com.kunmi.taskManager.repository.userRepo.UserRepositoryImpl;
import com.kunmi.taskManager.service.command.CommandServiceImpl;
import com.kunmi.taskManager.service.project.ProjectService;
import com.kunmi.taskManager.service.project.ProjectServiceImpl;
import com.kunmi.taskManager.service.user.UserService;
import com.kunmi.taskManager.service.user.UserServiceImpl;

public class App {

    public static void main( String[] args ) {


        System.out.println("==========================");
        System.out.println("TASK MANAGEMENT SYSTEM");
        System.out.println("==========================");
        System.out.println("Type 'help' for a list of available commands.");
        System.out.println("Type 'registration' to register.");
        System.out.println("Type 'login' to log in.");
        System.out.println("Type 'project' to list all projects");
        System.out.println("Type 'create project' to create a project");
        System.out.println("Type 'update project' to rename a project");
        System.out.println("Type 'delete project' to delete a project");

        IUserRepository userRepository = new UserRepositoryImpl();
        CommandRepository commandPersistence = new CommandRepositoryImpl();
        ProjectRepository projectRepository = new ProjectRepositoryImpl();

        ProjectService projectService = new ProjectServiceImpl(projectRepository);
        UserService userService = new UserServiceImpl(userRepository, projectService);
        CommandServiceImpl commandService = new CommandServiceImpl(userService, projectService, commandPersistence);

        boolean isRunning = true;

        while (isRunning) {
            String userInput = "\nEnter your command: ";
            String input = ScannerUtil.getString(userInput);

            if (input.equals("exit")) {
                System.out.println("Exiting Task Management System. Goodbye!");
                isRunning = false;
            } else {
                commandService.executeCommand(input);
                System.out.println("waiting next command...");
            }
        }
        ScannerUtil.closeScanner();
    }

}
