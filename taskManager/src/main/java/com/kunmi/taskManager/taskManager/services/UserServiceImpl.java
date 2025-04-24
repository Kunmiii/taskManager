package com.kunmi.taskManager.taskManager.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.kunmi.taskManager.taskManager.dto.*;
import com.kunmi.taskManager.taskManager.contexts.UserContext;
import com.kunmi.taskManager.taskManager.exceptions.InvalidCredentialsException;
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
    public RegisterUserResponseDTO registerUser(RegisteruserRequestDTO registeruserRequestDTO) {
        String hashedPassword = encryptPassword(registeruserRequestDTO);
        User user = new User(registeruserRequestDTO.firstName(),
                             registeruserRequestDTO.lastName(),
                             hashedPassword,
                             registeruserRequestDTO.email()
                             );

        try {
            User savedUser = userRepository.save(user);
            log.info("user with email: {} registered successfully", registeruserRequestDTO.email());
            return new RegisterUserResponseDTO(savedUser.getId(),
                                                  savedUser.getFirstName(),
                                                  savedUser.getLastName(),
                                                  savedUser.getEmail(),
                                                  savedUser.getCreatedAt());
        } catch (Exception e) {
            log.error("user with email: {} already registered", registeruserRequestDTO.email());
            throw new RuntimeException("User registration failed: " + e.getMessage(), e);
        }
    }

    private static @NotNull String encryptPassword(RegisteruserRequestDTO registeruserRequestDTO) {
        return BCrypt.withDefaults()
                .hashToString(12, registeruserRequestDTO
                                .password()
                                .toCharArray());
    }

    @Override
    public UserLoginResponseDTO userLogin(UserLoginRequestDTO userLoginRequestDTO) {
        User user = userRepository.getUserByEmail(userLoginRequestDTO.email())
                .orElseThrow(() -> new UserNotFoundException("Username does not exist"));

        boolean passwordMatches = isPasswordMatches(userLoginRequestDTO, user);

        if (!passwordMatches) {
            throw new InvalidCredentialsException("Invalid password");
        } else {
            UserContext.setCurrentUser(user);
            log.info("User {} logged in successfully", user.getEmail());
            return new UserLoginResponseDTO(user.getEmail() + " logged in successfully");
        }
    }

    private static boolean isPasswordMatches(UserLoginRequestDTO userLoginRequestDTO, User user) {
        return BCrypt.verifyer()
                .verify(userLoginRequestDTO.password().toCharArray(), user.getPassword())
                .verified;
    }
}
