package com.kunmi.taskManager.repository.userRepo;


import com.kunmi.taskManager.service.user.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepositoryImpl implements UserRepository {

    private final Map<String, User> userMemory = new HashMap<>();

    @Override
    public void saveUser(User user) {
        userMemory.put(user.getEmail(), user);
    }

    @Override
    public boolean userExists(String email) {
        return userMemory.containsKey(email);
    }

    @Override
    public User findUserByEmail(String email) {
        return userMemory.get(email);
    }
}
