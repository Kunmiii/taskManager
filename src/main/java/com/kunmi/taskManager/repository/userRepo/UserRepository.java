package com.kunmi.taskManager.repository.userRepo;

import com.kunmi.taskManager.service.user.User;

public interface UserRepository {
    void saveUserToDatabase(User user);
    void saveUser(User user);
    boolean userExists(String email);
    User getUserFromDatabase(String email);
    User getUser(String email);
}
