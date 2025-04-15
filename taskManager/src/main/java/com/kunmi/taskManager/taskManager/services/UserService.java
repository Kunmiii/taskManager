package com.kunmi.taskManager.taskManager.services;

import com.kunmi.taskManager.taskManager.records.*;

public interface UserService {
    RegisterUserResponseRecord registerUser(RegisteruserRequestRecord registeruserRequestRecord);
    UserLoginResponseRecord userLogin(UserLoginRequestRecord userLoginRequestRecord);


}
