package com.kunmi.taskManager.user;

public class UserFactory {

    public static User createUser(String email, String password, String name, String lastName) {

        if (email == null || password == null || password.isEmpty()) {
            System.out.println("Invalid username or password");
            return null;
        }

        return new User(email, password, name, lastName);
    }
}
