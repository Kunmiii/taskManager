package com.kunmi.taskManager.service.user;

import com.kunmi.taskManager.repository.userRepo.IUserRepository;
import org.mindrot.jbcrypt.BCrypt;

public class UserServiceImpl implements UserService {

    private final IUserRepository userRepository;

    public UserServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String registerUser(String name, String lastName, String email, String password) {
        String hashedPassword = hashPassword(password);
        User user = new User(name, lastName, hashedPassword, email);

        if (!userRepository.userExists(user.getEmail())) {
            userRepository.saveUser(user);

            return "User registered successfully.";
        }
        return "User with the email " + user.getEmail() + " already exist";
    }

    public String userLogin(String email, String password) {
        User user = userRepository.findUserByEmail(email);

        if (user != null) {
            if (checkPassword(password)) {
                UserContext.setCurrentUser(user);
                return "Login successful!";
            }
        }
        return "Login failed!";
    }

    private static String hashPassword(String plaintextPassword) {
        return BCrypt.hashpw(plaintextPassword, BCrypt.gensalt());
    }

    public boolean checkPassword(String plaintextPassword) {
        return BCrypt.checkpw(plaintextPassword, hashPassword(plaintextPassword));
    }
}


