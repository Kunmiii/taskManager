package com.kunmi.taskManager;

import com.kunmi.taskManager.repository.CommandRepositoryImpl;
import com.kunmi.taskManager.repository.UserRepositoryImpl;
import com.kunmi.taskManager.scannerUtil.ScannerUtil;
import com.kunmi.taskManager.service.CommandServiceImpl;
import com.kunmi.taskManager.service.UserService;
import com.kunmi.taskManager.service.UserServiceImpl;

public class App {

    public static void main( String[] args ) {


        System.out.println("==========================");
        System.out.println("TASK MANAGEMENT SYSTEM");
        System.out.println("==========================");
        System.out.println("Type 'help' for a list of available commands.");
        System.out.println("Type 'registration' to register.");
        System.out.println("Type 'login' to log in.");

        UserRepositoryImpl userPersistence = new UserRepositoryImpl();
        CommandRepositoryImpl commandPersistence = new CommandRepositoryImpl();
        UserService userService = new UserServiceImpl(userPersistence);
        CommandServiceImpl commandService = new CommandServiceImpl(userService, commandPersistence);

        String userInput = "\nEnter your command: ";
        String input = ScannerUtil.getString(userInput);
        commandService.executeCommand(input);
        ScannerUtil.closeScanner();
    }

}
