package com.kunmi.taskManager.service.user;

public interface User {
    boolean checkPassword(String password);
    String getEmail();
}
