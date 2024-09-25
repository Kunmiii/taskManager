package com.kunmi.taskManager;

import com.kunmi.taskManager.utilityManager.ScannerUtil;

import java.util.InputMismatchException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {

        System.out.println("==========================");
        System.out.println("TASK MANAGEMENT SYSTEM");
        System.out.println("==========================");
        System.out.println("1. Help");
        System.out.println("2. Registration");
        System.out.println("3. Login");


        String userInput = "\nEnter your choice: ";
        int input = ScannerUtil.getInt(userInput);
        try {
            switch (input) {
                case 1:
                    System.out.println("Help Command List");
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
                    break;
                case 2:
                    System.out.println("Registration");
                    break;
                case 3:
                    System.out.println("Login");
                    break;

                default:
                    System.out.println("Time out!");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input");
        }
    }
}
