package com.kunmi.taskManager.taskManager.services;

import com.kunmi.taskManager.taskManager.dto.*;

public interface UserService {
    RegisterUserResponseDTO registerUser(RegisteruserRequestDTO registeruserRequestDTO);
    UserLoginResponseDTO userLogin(UserLoginRequestDTO userLoginRequestDTO);


}
