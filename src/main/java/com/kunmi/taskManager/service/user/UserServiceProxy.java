package com.kunmi.taskManager.service.user;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceProxy implements UserService {

    private final UserService realUserService;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceProxy.class);

    public UserServiceProxy(UserService realUserService) {
        this.realUserService = realUserService;
    }
    @Override
    public String registerUser(String name, String lastName, String password, String email) {
        logger.info("Register user is being accessed for email {}", email);

        try {
            String result = realUserService.registerUser(name, lastName, password, email);
            logger.info("User registration successful for email {}", email);
            return result;
        } catch (Exception e) {
            logger.error("Error during user registration for email: {}", email, e);
            throw e;
        }
    }

    @Override
    public String userLogin(String email, String password) {
        logger.info("User login is being accessed with email: {}", email);

        try {
            String result = realUserService.userLogin(email, password);
            logger.info("User logged in successfully");
            return result;
        } catch (Exception e) {
            logger.error("Error during user login for email: {}", email, e);
            throw e;
        }
    }

    @Override
    public boolean checkPassword(String plaintextPassword, String storedHash) {
        logger.info("Password checked is being performed");

        try {
            boolean isMatch = BCrypt.checkpw(plaintextPassword, storedHash);
            logger.info("Password check completed: {}", isMatch ? "Match" : "No match");
            return isMatch;
        } catch (Exception e) {
            logger.error("Error during password check");
            throw e;
        }
    }
}
