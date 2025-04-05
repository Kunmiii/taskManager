package com.kunmi.taskManager.taskManager.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.kunmi.taskManager.taskManager.DTOs.*;
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
    public RegisterUserResponseDTO registerUser(RegisterUserRequestDTO registerUserRequestDTO) {
        String hashedPassword = encryptPassword(registerUserRequestDTO);
        User user = new User(registerUserRequestDTO.getFirstName(),
                             registerUserRequestDTO.getLastName(),
                             hashedPassword,
                             registerUserRequestDTO.getEmail()
                             );

        try {
            User savedUser = userRepository.save(user);
            log.info("user with email: {} registered successfully", registerUserRequestDTO.getEmail());
            return new RegisterUserResponseDTO(savedUser);
        } catch (Exception e) {
            log.error("user with email: {} already registered", registerUserRequestDTO.getEmail());
            throw new RuntimeException("User registration failed: " + e.getMessage(), e);
        }
    }

    private static @NotNull String encryptPassword(RegisterUserRequestDTO registerUserRequestDTO) {
        return BCrypt.withDefaults()
                .hashToString(12, registerUserRequestDTO
                                .getPassword()
                                .toCharArray());
    }

    @Override
    public UserLoginResponseDTO userLogin(UserLoginRequestDTO userLoginRequestDTO) {
        User user = userRepository.getUserByEmail(userLoginRequestDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Username does not exist"));

        boolean passwordMatches = isPasswordMatches(userLoginRequestDTO, user);

        if (!passwordMatches) {
            throw new RuntimeException("Invalid password");
        }
        UserContext.setCurrentUser(user);
        log.info("User {} logged in successfully", user.getEmail());

        return new UserLoginResponseDTO("User: " + user.getEmail() + " logged in successfully");
    }

    private static boolean isPasswordMatches(UserLoginRequestDTO userLoginRequestDTO, User user) {
        return BCrypt.verifyer()
                .verify(userLoginRequestDTO.getPassword().toCharArray(), user.getPassword())
                .verified;
    }
}
