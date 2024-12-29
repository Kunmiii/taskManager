package com.kunmi.taskManager.service.user;

import com.kunmi.taskManager.exceptions.UserAlreadyExistsException;
import com.kunmi.taskManager.exceptions.UserNotFoundException;
import com.kunmi.taskManager.repository.userRepo.UserRepository;
import com.kunmi.taskManager.utils.validation.ValidationUtils;
import lombok.SneakyThrows;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String registerUser(String name, String lastName, String password, String email) {
        try {
            ValidationUtils.validateInputs(name, "Name");
            ValidationUtils.validateInputs(lastName, "LastName");
            ValidationUtils.validateInputs(email, "Email");
            ValidationUtils.validateInputs(password, "Password");

            boolean userExists = userRepository.userExists(email);
            ValidationUtils.validateUserExists(userExists, email);

            String hashedPassword = hashPassword(password);

            User user = new User(name, lastName, hashedPassword, email);
            userRepository.saveUser(user);

            logger.info("User with ID {} registered successfully", user.getId());
            return "User registered successfully";

        } catch (IllegalArgumentException e ) {
            logger.error("Validation error during registration: {}", e.getMessage());
            return e.getMessage();
        } catch (UserAlreadyExistsException e){
            logger.error("Error registering user: {}", e.getMessage());
            return e.getMessage();
        } catch (Exception e) {
            logger.error("Unexpected error occur during registration: {}", e.getMessage());
            throw new RuntimeException("An unexpected error occurred during registration");
        }
    }

    @SneakyThrows
    public String userLogin(String email, String password) {
        try {
            ValidationUtils.validateInputs(email, "Email");
            ValidationUtils.validateInputs(password, "Password");

            User user = userRepository.getUser(email);
            ValidationUtils.validateUserLogin(user);

            if (!checkPassword(password, user.getPassword())) {
                logger.info("Invalid login attempt for user with email {}", email);
                return "Invalid email or password";
            }

            UserContext.setCurrentUser(user);

            logger.info("User with ID {} logged in successfully", user.getId());
            return "Login successful";


        } catch (IllegalArgumentException | UserNotFoundException e) {
            logger.error("Error occurred: {}", e.getMessage());
            return e.getMessage();
        } catch (Exception e) {
            logger.error("Error occurred while logging in: {}", e.getMessage());
            throw new RuntimeException("Unexpected error occurred during login");
        }
    }

    private static String hashPassword(String plaintextPassword) {
        return BCrypt.hashpw(plaintextPassword, BCrypt.gensalt());
    }

    public boolean checkPassword(String plaintextPassword, String storedHash) {
        return BCrypt.checkpw(plaintextPassword, storedHash);
    }
}


