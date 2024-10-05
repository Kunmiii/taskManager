package com.kunmi.taskManager.user;

import java.util.HashMap;
import java.util.Map;

import static com.kunmi.taskManager.user.UserFactory.createUser;

public class UserService {

    private static final Map<String, User> userMemory = new HashMap<>();

    public static String registerUser(String name, String lastName, String email, String password) {
        User user = createUser(email, password, name, lastName);

        if (user != null) {
            if (!validateExistingUser(user)) {
                userMemory.put(user.getEmail(), user);
                return "User registered successfully.";
            }
            return "User with the email " + user.getEmail() + " already exist";
        }
        return "Error: Invalid input!";
    }

    public static String userLogin(String email, String password) {
        User user = userMemory.get(email);

        if (user != null && user.checkPassword(password)) {
            return "Login successful!";
        } else {
            return "Login failed! Invalid email or password!";
        }
    }

    private static boolean validateExistingUser(User user) {
       return (userMemory.containsKey(user.getEmail()));
    }
}
