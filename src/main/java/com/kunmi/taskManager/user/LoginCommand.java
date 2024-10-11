package com.kunmi.taskManager.user;

public class LoginCommand implements Command {
    private final UserManagement userManagement;

    public LoginCommand(UserManagement userManagement) {
        this.userManagement = userManagement;
    }
    @Override
    public String getName() {
        return "login";
    }

    @Override
    public void execute() {
        userManagement.handleUserLogin();
    }
}
