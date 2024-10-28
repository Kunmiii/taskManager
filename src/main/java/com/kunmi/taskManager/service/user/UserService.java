package com.kunmi.taskManager.service.user;

public interface UserService {
    String registerUser(String name, String lastName, String email, String password);
    String userLogin(String email, String password);
}
