package com.kunmi.taskManager.user;

import lombok.*;

@Getter
@EqualsAndHashCode
@ToString

public class User implements IUser {

    private final String name;
    private final String lastName;
    private final String password;
    private final String email;

    public User(String name, String lastName, String password, String email) {
        this.name = name;
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
}
