package com.kunmi.taskManager.taskManager.controllers;

import com.kunmi.taskManager.taskManager.DTOs.RegisterUserRequestDTO;
import com.kunmi.taskManager.taskManager.DTOs.RegisterUserResponseDTO;
import com.kunmi.taskManager.taskManager.DTOs.UserLoginRequestDTO;
import com.kunmi.taskManager.taskManager.DTOs.UserLoginResponseDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

public interface UserController {
    ResponseEntity<RegisterUserResponseDTO> registration(RegisterUserRequestDTO registerUserRequestDTO);
    ResponseEntity<UserLoginResponseDTO> login(UserLoginRequestDTO userLoginRequestDTO, HttpSession session);
}
