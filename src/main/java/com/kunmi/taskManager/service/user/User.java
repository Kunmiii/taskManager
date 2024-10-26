package com.kunmi.taskManager.service.user;

import com.kunmi.taskManager.service.project.Project;

public interface User {
    boolean checkPassword(String password);
    String getEmail();
    void addProject(Project project);
}
