package com.kunmi.taskManager.user;

import com.kunmi.taskManager.project.Project;

public interface IUser {
    boolean checkPassword(String password);
    String getEmail();
    void addProject(Project project);
}
