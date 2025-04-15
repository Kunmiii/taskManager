package com.kunmi.taskManager.taskManager.records;

import com.kunmi.taskManager.taskManager.exceptions.ExceptionResponseBuilder;

import java.util.Set;

public record ExceptionResponse(
        Integer businessErrorCode,
        String businessErrorDescription,
        String error,
        Set<String> validationErrors
) {
    public static ExceptionResponseBuilder builder() {
        return new ExceptionResponseBuilder();
    }
}
