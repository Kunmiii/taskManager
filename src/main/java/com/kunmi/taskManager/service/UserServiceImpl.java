package com.kunmi.taskManager.service;

import com.kunmi.taskManager.user.IUser;
import com.kunmi.taskManager.user.User;
import com.kunmi.taskManager.persistence.UserPersistenceImpl;

public class UserServiceImpl implements UserService {

    private final UserPersistenceImpl userPersistenceImpl;

    public UserServiceImpl(UserPersistenceImpl userPersistenceImpl) {
        this.userPersistenceImpl = userPersistenceImpl;
    }

    public String registerUser(String name, String lastName, String email, String password) {
        User user = new User(email, password, name, lastName);

        if (!userPersistenceImpl.userExists(user.getEmail())) {
            userPersistenceImpl.saveUser(user);
            return "User registered successfully.";
        }
        return "User with the email " + user.getEmail() + " already exist";
    }

    public String userLogin(String email, String password) {
        IUser user = userPersistenceImpl.findUserByEmail(email);

        if (user != null && user.checkPassword(password)) {
            return "Login successful!";
        } else {
            return "Login failed!";
        }
    }
}
