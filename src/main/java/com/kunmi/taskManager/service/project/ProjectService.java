package com.kunmi.taskManager.service.project;

import com.kunmi.taskManager.service.user.UserImpl;

import java.time.LocalDateTime;

public interface ProjectService {
    void setLoggedInUser(UserImpl loggedInUserImpl);
    void create(String projectId, String projectName, LocalDateTime createDate);
    void update(String projectId, String projectName);
    void view();
    void delete(String id);

}
