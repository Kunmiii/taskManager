package com.kunmi.taskManager.service.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@EqualsAndHashCode
@ToString

public class User {

    private static final AtomicInteger idCounter = new AtomicInteger(1);
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String password;
    private final String email;

    public User(String firstName, String lastName, String password, String email) {
        this.id = generateId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    private static String generateId() {
        return String.valueOf(idCounter.getAndIncrement());
    }

    public static User fromString(String data) {
        String[] parts = data.split(",");
        return new User(parts[0], parts[1], parts[2], parts[3]);
    }

}
