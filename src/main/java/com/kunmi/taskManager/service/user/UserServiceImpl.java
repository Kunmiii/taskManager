package com.kunmi.taskManager.service.user;

import com.kunmi.taskManager.repository.userRepo.IUserRepository;
import com.kunmi.taskManager.service.project.ProjectService;

public class UserServiceImpl implements UserService {

    private final IUserRepository userRepositoryImpl;
    private final ProjectService projectService;

    public UserServiceImpl(IUserRepository userRepositoryImpl, ProjectService projectService) {
        this.userRepositoryImpl = userRepositoryImpl;
        this.projectService = projectService;
    }

    public String registerUser(String id, String name, String lastName, String email, String password) {
        UserImpl userImpl = new UserImpl(id, name, lastName, password, email);

        if (!userRepositoryImpl.userExists(userImpl.getEmail())) {
            userRepositoryImpl.saveUser(userImpl);

            return "User registered successfully.";
        }
        return "User with the email " + userImpl.getEmail() + " already exist";
    }

    public String userLogin(String email, String password) {
        UserImpl userImpl = (UserImpl) userRepositoryImpl.findUserByEmail(email);

        if (userImpl != null && userImpl.checkPassword(password)) {
            projectService.setLoggedInUser(userImpl);
            return "Login successful!";
        } else {
            return "Login failed!";
        }
    }
}
