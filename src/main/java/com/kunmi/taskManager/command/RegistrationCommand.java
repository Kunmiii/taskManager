package com.kunmi.taskManager.command;

import com.kunmi.taskManager.scannerUtil.ScannerUtil;
import com.kunmi.taskManager.service.CommandService;
import com.kunmi.taskManager.service.UserService;

public class RegistrationCommand implements CommandService {

    private final UserService userService;

    public RegistrationCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute() {
        System.out.println("====================");
        System.out.println("User Registration");
        System.out.println("====================");

        String firstName = ScannerUtil.getString("Enter first name: ");
        String lastName = ScannerUtil.getString("Enter last name: ");
        String password = ScannerUtil.getString("Enter password: ");
        String email = ScannerUtil.getString("Enter email address: ");

        String registrationResult = userService.registerUser(firstName, lastName, email, password);
        System.out.println(registrationResult);
    }

    @Override
    public String getName() {
        return "registration";
    }
}