package com.kunmi.taskManager.persistence;

import com.kunmi.taskManager.user.IUser;
import com.kunmi.taskManager.user.User;

public interface IUserPersistence {
    void saveUser(User user);
    boolean userExists(String email);
    IUser findUserByEmail(String email);
}
