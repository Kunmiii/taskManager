package com.kunmi.taskManager.service.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString

public class UserImpl implements User {

    private static int idCounter = 1;
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String password;
    private final String email;

    public UserImpl(String firstName, String lastName, String password, String email) {
        this.id = generateId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String getEmail() {
        return email;
    }

    private static String generateId() {
        return String.valueOf(idCounter++);
    }
}
