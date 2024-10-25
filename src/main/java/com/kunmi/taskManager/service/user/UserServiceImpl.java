package com.kunmi.taskManager.service.user;

import com.kunmi.taskManager.repository.userRepo.IUserRepository;

public class UserServiceImpl implements UserService {

    private final IUserRepository userRepositoryImpl;

    public UserServiceImpl(IUserRepository userRepositoryImpl) {
        this.userRepositoryImpl = userRepositoryImpl;
    }

    public String registerUser(String id, String name, String lastName, String email, String password) {
        User user = new User(id, email, password, name, lastName);

        if (!userRepositoryImpl.userExists(user.getEmail())) {
            userRepositoryImpl.saveUser(user);
            return "User registered successfully.";
        }
        return "User with the email " + user.getEmail() + " already exist";
    }

    public String userLogin(String email, String password) {
        IUser user = userRepositoryImpl.findUserByEmail(email);

        if (user != null && user.checkPassword(password)) {
            return "Login successful!";
        } else {
            return "Login failed!";
        }
    }
}