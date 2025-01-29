package com.kunmi.taskManager.repository.userRepo;

import com.kunmi.taskManager.models.User;

import java.util.List;

public interface UserRepository {
    void saveUser(User user);
    User getUserByEmail(String email);
    void updateUser(User user);
    void deleteUser(String email);
    List<User> getAllUsers();
    boolean userExists(String email);
}
