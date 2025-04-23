package com.kunmi.taskManager.taskManager.exceptions.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BusinessErrorCodes {

    USER_NOT_FOUND(1001, "User not found"),
    PROJECT_NOT_FOUND(1002, "project not found"),
    INVALID_SESSION(1003, "invalid or expired session"),
    BAD_REQUEST(1004, "Invalid request data"),
    INVALID_CREDENTIALS(1005, "Invalid email or password"),
    USER_ALREADY_EXISTS(1006, "User already exists")
    ;

    private final int code;
    private final String description;
}
