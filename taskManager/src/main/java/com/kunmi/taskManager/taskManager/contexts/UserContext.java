package com.kunmi.taskManager.taskManager.contexts;

import com.kunmi.taskManager.taskManager.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserContext {

    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

    public static void setCurrentUser(User user) {
        currentUser.set(user);
    }

    public static User getCurrentUser() {
        return currentUser.get();
    }

    public static void clear() {
        currentUser.remove();
    }

}
