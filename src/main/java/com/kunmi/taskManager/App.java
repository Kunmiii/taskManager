package com.kunmi.taskManager;

import com.kunmi.taskManager.scannerUtil.ScannerUtil;
import com.kunmi.taskManager.user.UserFactory;
import com.kunmi.taskManager.user.UserService;

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
        String email, password;

        try {
            switch (input) {
                case 1:
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
                    break;
                case 2:
                    System.out.println("====================");
                    System.out.println("User Registration");
                    System.out.println("====================");
                    String firstName = "Enter first name: ";
                    firstName = ScannerUtil.getString(firstName);
                    String lastName = "Enter last name: ";
                    lastName = ScannerUtil.getString(lastName);
                    password = "Enter password: ";
                    password = ScannerUtil.getString(password);
                    email = "Enter email address: ";
                    email = ScannerUtil.getString(email);

                    UserFactory.createUser(email, password, firstName, lastName);

                    break;
                case 3:
                    System.out.println("====================");
                    System.out.println("User Login");
                    System.out.println("====================");

                    email = ScannerUtil.getString("Enter email: ");
                    password = ScannerUtil.getString("Enter password: ");

                    String result = UserService.userLogin(email, password);
                    System.out.printf(result);
                    break;

                default:
                    System.out.println("Time out!");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input");
        }
        ScannerUtil.closeScanner();
    }
}
