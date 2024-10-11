package com.kunmi.taskManager.user;

import lombok.*;

@Getter
@EqualsAndHashCode
@ToString

public class User implements UserInterface {

    private String name;
    private String lastName;
    private String password;
    private String email;

    User(String name, String lastName, String password, String email) {
        this.name = name;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
