package com.kunmi.taskManager.service.command;

import com.kunmi.taskManager.utils.input.ScannerUtil;
import com.kunmi.taskManager.service.user.UserService;

public class RegistrationCommand implements Command {

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

        String registrationResult = userService.registerUser(firstName, lastName, password, email);
        System.out.println(registrationResult);
    }

    @Override
    public String getName() {
        return "registration";
    }
}
