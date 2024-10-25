package com.kunmi.taskManager.repository;

import com.kunmi.taskManager.user.IUser;
import com.kunmi.taskManager.user.User;

public interface IUserRepository {
    void saveUser(User user);
    boolean userExists(String email);
    IUser findUserByEmail(String email);
}
