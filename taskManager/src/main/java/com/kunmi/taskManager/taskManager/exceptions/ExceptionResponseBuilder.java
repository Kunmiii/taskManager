package com.kunmi.taskManager.taskManager.exceptions;

import java.util.Set;

public class ExceptionResponseBuilder {

    private Integer businessErrorCode;
    private String businessErrorDescription;
    private String error;
    private Set<String> validateErrors;

    public ExceptionResponseBuilder businessErrorCode(Integer code) {
        this.businessErrorCode = code;
        return this;
    }

    public ExceptionResponseBuilder businessErrorDescription(String description) {
        this.businessErrorDescription = description;
        return this;
    }

    public ExceptionResponseBuilder error(String error) {
        this.error = error;
        return this;
    }

    public ExceptionResponseBuilder validateErrors(Set<String> validateErrors) {
        this.validateErrors = validateErrors;
        return this;
    }

    public ExceptionResponse build() {
        return new ExceptionResponse(businessErrorCode, businessErrorDescription, error, validateErrors);
    }
}
