package com.kunmi.taskManager.command;

import com.kunmi.taskManager.scannerUtil.ScannerUtil;
import com.kunmi.taskManager.service.CommandService;
import com.kunmi.taskManager.service.UserService;

public class LoginCommand implements CommandService {

    private final UserService userService;

    public LoginCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute() {
        System.out.println("====================");
        System.out.println("User Login");
        System.out.println("====================");

        String email = ScannerUtil.getString("Enter email: ");
        String password = ScannerUtil.getString("Enter password: ");

        String result = userService.userLogin(email, password);
        System.out.printf(result);
    }

    @Override
    public String getName() {
        return "login";
    }
}
