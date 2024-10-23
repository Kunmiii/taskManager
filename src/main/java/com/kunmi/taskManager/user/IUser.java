package com.kunmi.taskManager.user;

public interface IUser {
    boolean checkPassword(String password);
    String getEmail();
}
