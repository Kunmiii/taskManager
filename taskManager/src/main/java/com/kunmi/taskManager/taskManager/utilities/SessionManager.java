package com.kunmi.taskManager.taskManager.utilities;

import com.kunmi.taskManager.taskManager.models.User;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SessionManager {

    public User getCurrentUser(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            log.error("No user logged in");
            throw new IllegalStateException("No user is currently logged in");
        }
        return currentUser;
    }

    public void setCurrentUser(HttpSession session, User user) {
        session.setAttribute("currentUser", user);
    }


}
