package com.kunmi.taskManager.repository.userRepo;

import com.kunmi.taskManager.service.user.User;

public interface IUserRepository {
    void saveUser(User user);
    boolean userExists(String email);
    User findUserByEmail(String email);
}
