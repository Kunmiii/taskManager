package com.kunmi.taskManager;

import com.kunmi.taskManager.scannerUtil.ScannerUtil;
import com.kunmi.taskManager.user.CommandService;
import com.kunmi.taskManager.user.IUserService;
import com.kunmi.taskManager.user.UserManagement;
import com.kunmi.taskManager.user.UserService;

public class App {

    public static void main( String[] args ) {


        System.out.println("==========================");
        System.out.println("TASK MANAGEMENT SYSTEM");
        System.out.println("==========================");
        System.out.println("Type 'help' for a list of available commands.");
        System.out.println("Type 'registration' to register.");
        System.out.println("Type 'login' to log in.");

        IUserService userService = UserService.getServices();
        UserManagement userManagement = new UserManagement(userService);
        CommandService commandService = new CommandService(userManagement);

        String userInput = "\nEnter your command: ";
        String input = ScannerUtil.getString(userInput);
        commandService.executeCommand(input);
        ScannerUtil.closeScanner();
    }

}
