package com.kunmi.taskManager;

import com.kunmi.taskManager.scannerUtil.ScannerUtil;
import com.kunmi.taskManager.user.UserManagement;
import com.kunmi.taskManager.user.UserManagementFactory;
import com.kunmi.taskManager.user.UserService;

import java.util.InputMismatchException;

public class App {

    public static void main( String[] args ) {


        System.out.println("==========================");
        System.out.println("TASK MANAGEMENT SYSTEM");
        System.out.println("==========================");
        System.out.println("1. Help");
        System.out.println("2. Registration");
        System.out.println("3. Login");

        UserService userService = UserService.getServices();
        UserManagementFactory userManagementFactory = new UserManagementFactory(userService);
        UserManagement userManagement = userManagementFactory.createUserManagement();

        String userInput = "\nEnter your choice: ";
        int input = ScannerUtil.getInt(userInput);

        try {
            switch (input) {
                case 1:
                    displayHelpCommands();
                    break;
                case 2:
                    userManagement.handleUserRegistration();
                    break;
                case 3:
                    userManagement.handleUserLogin();
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input");
        }
        ScannerUtil.closeScanner();
    }

    private static void displayHelpCommands() {
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
