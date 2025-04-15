package com.kunmi.taskManager.taskManager.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.kunmi.taskManager.taskManager.records.*;
import com.kunmi.taskManager.taskManager.contexts.UserContext;
import com.kunmi.taskManager.taskManager.exceptions.UserNotFoundException;
import com.kunmi.taskManager.taskManager.models.User;
import com.kunmi.taskManager.taskManager.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public RegisterUserResponseRecord registerUser(RegisteruserRequestRecord registeruserRequestRecord) {
        String hashedPassword = encryptPassword(registeruserRequestRecord);
        User user = new User(registeruserRequestRecord.firstName(),
                             registeruserRequestRecord.lastName(),
                             hashedPassword,
                             registeruserRequestRecord.email()
                             );

        try {
            User savedUser = userRepository.save(user);
            log.info("user with email: {} registered successfully", registeruserRequestRecord.email());
            return new RegisterUserResponseRecord(savedUser.getId(),
                                                  savedUser.getFirstName(),
                                                  savedUser.getLastName(),
                                                  savedUser.getEmail(),
                                                  savedUser.getCreatedAt());
        } catch (Exception e) {
            log.error("user with email: {} already registered", registeruserRequestRecord.email());
            throw new RuntimeException("User registration failed: " + e.getMessage(), e);
        }
    }

    private static @NotNull String encryptPassword(RegisteruserRequestRecord registeruserRequestRecord) {
        return BCrypt.withDefaults()
                .hashToString(12, registeruserRequestRecord
                                .password()
                                .toCharArray());
    }

    @Override
    public UserLoginResponseRecord userLogin(UserLoginRequestRecord userLoginRequestRecord) {
        User user = userRepository.getUserByEmail(userLoginRequestRecord.email())
                .orElseThrow(() -> new UserNotFoundException("Username does not exist"));

        boolean passwordMatches = isPasswordMatches(userLoginRequestRecord, user);

        if (!passwordMatches) {
            throw new RuntimeException("Invalid password");
        }
        UserContext.setCurrentUser(user);
        log.info("User {} logged in successfully", user.getEmail());

        return new UserLoginResponseRecord("User: " + user.getEmail() + " logged in successfully");
    }

    private static boolean isPasswordMatches(UserLoginRequestRecord userLoginRequestRecord, User user) {
        return BCrypt.verifyer()
                .verify(userLoginRequestRecord.password().toCharArray(), user.getPassword())
                .verified;
    }
}
