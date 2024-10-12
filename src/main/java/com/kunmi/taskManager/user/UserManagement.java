package com.kunmi.taskManager.user;

import com.kunmi.taskManager.scannerUtil.ScannerUtil;

public class UserManagement {

    private final IUserService userService;

    public UserManagement(IUserService userService) {
        this.userService = userService;
    }

    public void handleUserLogin() {

        System.out.println("====================");
        System.out.println("User Login");
        System.out.println("====================");

        String email = ScannerUtil.getString("Enter email: ");
        String password = ScannerUtil.getString("Enter password: ");

        String result = userService.userLogin(email, password);
        System.out.printf(result);
    }

    public void handleUserRegistration() {
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
}
