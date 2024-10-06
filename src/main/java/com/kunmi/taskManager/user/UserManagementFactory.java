package com.kunmi.taskManager.user;

public class UserManagementFactory {

    private final UserService userService;

    public UserManagementFactory(UserService userService) {
        this.userService = userService;
    }

    public UserManagement createUserManagement() {
        return new UserManagement(userService);
    }
}
