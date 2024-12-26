package com.kunmi.taskManager.utils.validation;

import com.kunmi.taskManager.exceptions.ProjectNotFoundException;
import com.kunmi.taskManager.exceptions.UserAlreadyExistsException;
import com.kunmi.taskManager.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class ValidationUtils {

    private static final Logger logger = LoggerFactory.getLogger(ValidationUtils.class);

    private ValidationUtils() {

    }

    public static void validateInputs(String input, String fieldName) {
        if (input == null || input.trim().isEmpty()) {
            logger.warn("{} cannot be null or empty", fieldName);
            throw new IllegalArgumentException(fieldName + " must be provided");
        }
    }

    public static void validateNotNull(Object obj, String fieldName) {
        try {
            Objects.requireNonNull(obj, "Object must not be null");
        } catch (NullPointerException e) {
            throw new NullPointerException(fieldName + " must not be null");
        }
    }

    public static void validateProjectExists(boolean projectExists, String projectId) throws ProjectNotFoundException {
        logger.debug("Checking if project ID {} exists in the repository", projectId);
        if (!projectExists) {
            throw new ProjectNotFoundException("Project with ID " + projectId + " does not exist");
        }
        logger.info("Project with ID {} exists", projectId);
    }

    public static void validateUserProjects(Object userProjects, String userId) throws ProjectNotFoundException {
        if (userProjects == null) {
            logger.warn("No projects found for user ID {}", userId);
            throw new ProjectNotFoundException("No projects found for user." + userId);
        }
    }

    public static void validateUserProject(Object userProject, String userID) throws ProjectNotFoundException {
        if (userProject == null) {
            logger.warn("No project found for user ID {}", userID);
            throw new ProjectNotFoundException("No projects found for user." + userID);
        }
    }

    public static void validateUserExists(boolean userExist, String email) throws UserAlreadyExistsException {
        if (userExist) {
            logger.info("User with the email {} already exist", email);
            throw new UserAlreadyExistsException("User with email " + email + " already exists.");
        }
    }

    public static void validateUserLogin(Object user) throws UserNotFoundException {
        if (user == null) {
            throw new UserNotFoundException("User does not have an account");
        }
    }
}
