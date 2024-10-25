package com.kunmi.taskManager.service.user;

import com.kunmi.taskManager.service.project.Project;

public interface IUser {
    boolean checkPassword(String password);
    String getEmail();
    void addProject(Project project);
}
