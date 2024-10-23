package com.kunmi.taskManager.persistence;

import com.kunmi.taskManager.user.IUser;
import com.kunmi.taskManager.user.User;

import java.util.HashMap;
import java.util.Map;

public class UserPersistenceImpl implements IUserPersistence {

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
    public IUser findUserByEmail(String email) {
        return userMemory.get(email);
    }
}
