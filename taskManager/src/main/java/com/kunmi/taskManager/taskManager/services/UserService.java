package com.kunmi.taskManager.taskManager.services;

import com.kunmi.taskManager.taskManager.DTOs.RegisterUserRequestDTO;
import com.kunmi.taskManager.taskManager.DTOs.RegisterUserResponseDTO;
import com.kunmi.taskManager.taskManager.DTOs.UserLoginRequestDTO;
import com.kunmi.taskManager.taskManager.DTOs.UserLoginResponseDTO;

public interface UserService {
    RegisterUserResponseDTO registerUser(RegisterUserRequestDTO registerUserRequestDTO);
    UserLoginResponseDTO userLogin(UserLoginRequestDTO userLoginRequestDTO);


}
