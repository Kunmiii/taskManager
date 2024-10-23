package com.kunmi.taskManager;

import com.kunmi.taskManager.persistence.CommandPersistenceImpl;
import com.kunmi.taskManager.scannerUtil.ScannerUtil;
import com.kunmi.taskManager.service.CommandServiceImpl;
import com.kunmi.taskManager.service.UserService;
import com.kunmi.taskManager.persistence.UserPersistenceImpl;
import com.kunmi.taskManager.service.UserServiceImpl;

public class App {

    public static void main( String[] args ) {


        System.out.println("==========================");
        System.out.println("TASK MANAGEMENT SYSTEM");
        System.out.println("==========================");
        System.out.println("Type 'help' for a list of available commands.");
        System.out.println("Type 'registration' to register.");
        System.out.println("Type 'login' to log in.");

        UserPersistenceImpl userPersistenceImpl = new UserPersistenceImpl();
        CommandPersistenceImpl commandPersistence = new CommandPersistenceImpl();
        UserService userService = new UserServiceImpl(userPersistenceImpl);
        CommandServiceImpl commandServiceImpl = new CommandServiceImpl(userService, commandPersistence);

        String userInput = "\nEnter your command: ";
        String input = ScannerUtil.getString(userInput);
        commandServiceImpl.executeCommand(input);
        ScannerUtil.closeScanner();
    }

}
