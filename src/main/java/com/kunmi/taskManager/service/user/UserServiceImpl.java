package com.kunmi.taskManager.service.user;

import com.kunmi.taskManager.repository.userRepo.IUserRepository;
import com.kunmi.taskManager.service.project.ProjectService;

public class UserServiceImpl implements UserService {

    private final IUserRepository userRepository;
    private final ProjectService projectService;

    public UserServiceImpl(IUserRepository userRepository, ProjectService projectService) {
        this.userRepository = userRepository;
        this.projectService = projectService;
    }

    public String registerUser(String name, String lastName, String email, String password) {
        UserImpl userImpl = new UserImpl(name, lastName, password, email);

        if (!userRepository.userExists(userImpl.getEmail())) {
            userRepository.saveUser(userImpl);

            return "User registered successfully.";
        }
        return "User with the email " + userImpl.getEmail() + " already exist";
    }

    public String userLogin(String email, String password) {
        User user = userRepository.findUserByEmail(email);

        if (user instanceof UserImpl) {
            UserImpl userImpl = (UserImpl) user;
            if (userImpl.checkPassword(password)) {
                projectService.setLoggedInUser(userImpl);
                return "Login successful!";
            }
        }
            return "Login failed!";
        }
    }
