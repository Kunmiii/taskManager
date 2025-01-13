package com.kunmi.taskManager.service.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString

public class User {

    private final String id;
    private final String firstName;
    private final String lastName;
    private final String password;
    private final String email;


    public User (String firstName, String lastName, String password, String email) {
        this.id = null;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    public User (String id, String firstName, String lastName, String password, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    public static User fromString(String data) {
        String[] parts = data.split(",");
        return new User(parts[0], parts[1], parts[2], parts[3]);
    }

}
