package com.kunmi.taskManager.service.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.mindrot.jbcrypt.BCrypt;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@EqualsAndHashCode
@ToString

public class UserImpl implements User {

    private static final AtomicInteger idCounter = new AtomicInteger(1);
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String hashedPassword;
    private final String email;

    public UserImpl(String firstName, String lastName, String password, String email) {
        this.id = generateId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.hashedPassword = hashPassword(password);
        this.email = email;
    }

    public boolean checkPassword(String plaintextPassword) {
        return BCrypt.checkpw(plaintextPassword, hashedPassword);
    }

    @Override
    public String getEmail() {
        return email;
    }

    private static String hashPassword(String plaintextPassword) {
        return BCrypt.hashpw(plaintextPassword, BCrypt.gensalt());
    }

    private static String generateId() {
        return String.valueOf(idCounter.getAndIncrement());
    }
}
