package com.kunmi.taskManager.taskManager.exceptions;

public class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
}
