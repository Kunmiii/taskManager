package com.kunmi.taskManager.user;

public interface IUserService {
    String registerUser(String name, String lastName, String email, String password);
    String userLogin(String email, String password);
}
