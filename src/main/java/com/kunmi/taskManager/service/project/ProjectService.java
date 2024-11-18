package com.kunmi.taskManager.service.project;

import com.kunmi.taskManager.service.user.UserImpl;

import java.time.LocalDateTime;

public interface ProjectService {
    void setLoggedInUser(UserImpl loggedInUserImpl);
    void create(String projectName, LocalDateTime createDate);
    void update(String projectId, String projectName);
    void view(String projectId);
    void delete(String id);
    UserImpl getLoggedInUser();

}
