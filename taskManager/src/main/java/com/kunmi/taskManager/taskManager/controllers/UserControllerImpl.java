package com.kunmi.taskManager.taskManager.controllers;

import com.kunmi.taskManager.taskManager.DTOs.RegisterUserRequestDTO;
import com.kunmi.taskManager.taskManager.DTOs.RegisterUserResponseDTO;
import com.kunmi.taskManager.taskManager.DTOs.UserLoginRequestDTO;
import com.kunmi.taskManager.taskManager.DTOs.UserLoginResponseDTO;
import com.kunmi.taskManager.taskManager.models.User;
import com.kunmi.taskManager.taskManager.repositories.UserRepository;
import com.kunmi.taskManager.taskManager.services.UserService;
import com.kunmi.taskManager.taskManager.utilities.SessionManager;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserControllerImpl implements UserController{

    private final UserService userService;
    private final UserRepository userRepository;
    private final SessionManager sessionManager;

    @Override
    @PostMapping("/registration")
    public ResponseEntity<RegisterUserResponseDTO> registration(@Valid @RequestBody RegisterUserRequestDTO registerUserRequestDTO) {
        RegisterUserResponseDTO responseDTO = userService.registerUser(registerUserRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    @PostMapping("/auth")
    public ResponseEntity<UserLoginResponseDTO> login(@Valid @RequestBody UserLoginRequestDTO userLoginRequestDTO, HttpSession session) {
        UserLoginResponseDTO response = userService.userLogin(userLoginRequestDTO);
        User user = userRepository.getUserByEmail(userLoginRequestDTO.getEmail()).get();

        sessionManager.setCurrentUser(session, user);
        log.info("User {} stored in session", user.getEmail());
        return ResponseEntity.ok(response);
    }

}
