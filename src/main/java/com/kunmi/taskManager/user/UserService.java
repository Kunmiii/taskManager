package com.kunmi.taskManager.user;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private static UserService userService;
    private final Map<String, User> userMemory = new HashMap<>();


    private UserService() {
    }

    public static UserService getServices() {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }

    public String registerUser(String name, String lastName, String email, String password) {
        User user = new User(email, password, name, lastName);

        if (!validateExistingUser(user)) {
            userMemory.put(user.getEmail(), user);
            return "User registered successfully.";
        }
        return "User with the email " + user.getEmail() + " already exist";
    }

    public String userLogin(String email, String password) {
        User user = userMemory.get(email);

        if (user != null && user.checkPassword(password)) {
            return "Login successful!";
        } else {
            return "Login failed! Invalid email or password!";
        }
    }

    private boolean validateExistingUser(User user) {
       return (userMemory.containsKey(user.getEmail()));
    }
}
