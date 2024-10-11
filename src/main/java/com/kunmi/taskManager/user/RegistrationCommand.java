package com.kunmi.taskManager.user;

public class RegistrationCommand implements Command {

    private final UserManagement userManagement;

    public RegistrationCommand(UserManagement userManagement) {
        this.userManagement = userManagement;
    }

    @Override
    public String getName() {
        return "registration";
    }

    @Override
    public void execute() {
        userManagement.handleUserRegistration();
    }
}
