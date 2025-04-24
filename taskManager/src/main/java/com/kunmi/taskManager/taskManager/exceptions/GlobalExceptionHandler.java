package com.kunmi.taskManager.taskManager.exceptions;

import com.kunmi.taskManager.taskManager.exceptions.enums.BusinessErrorCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFound(UserNotFoundException ex) {
        log.info("User not found: {}", ex.getMessage());
        return ResponseEntity.status(NOT_FOUND)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.USER_NOT_FOUND.getCode())
                        .businessErrorDescription(BusinessErrorCodes.USER_NOT_FOUND.getDescription())
                        .error(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleProjectNotFound(ProjectNotFoundException ex) {
        log.info("Project not found: {}", ex.getMessage());
        return ResponseEntity.status(NOT_FOUND)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.PROJECT_NOT_FOUND.getCode())
                        .businessErrorDescription(BusinessErrorCodes.PROJECT_NOT_FOUND.getDescription())
                        .error(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidSession(IllegalStateException ex) {
        log.info("Session error: {}", ex.getMessage());
        return ResponseEntity.status(UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.INVALID_SESSION.getCode())
                        .businessErrorDescription(BusinessErrorCodes.INVALID_SESSION.getDescription())
                        .error(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        log.info("Invalid credentials: {}", ex.getMessage());
        return ResponseEntity.status(UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.INVALID_CREDENTIALS.getCode())
                        .businessErrorDescription(BusinessErrorCodes.INVALID_CREDENTIALS.getDescription())
                        .error(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ExceptionResponse> handleUserAlreadyExists(UserAlreadyExistException ex) {
        log.info("User already exists: {}", ex.getMessage());
        return ResponseEntity.status(UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.USER_ALREADY_EXISTS.getCode())
                        .businessErrorDescription(BusinessErrorCodes.USER_ALREADY_EXISTS.getDescription())
                        .error(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Set<String> errors = new HashSet<>();
        ex.getBindingResult().getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        log.info("Validation errors: {}", errors);
        return ResponseEntity.status(BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.BAD_REQUEST.getCode())
                        .businessErrorDescription(BusinessErrorCodes.BAD_REQUEST.getDescription())
                        .validateErrors(errors)
                        .build());
    }

    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .businessErrorDescription("Internal server error, please contact admin")
                        .error(ex.getMessage())
                        .build());
    }

}
