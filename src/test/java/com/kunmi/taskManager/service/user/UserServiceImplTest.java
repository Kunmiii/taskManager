package com.kunmi.taskManager.service.user;

import com.kunmi.taskManager.repository.userRepo.IUserRepository;
import com.kunmi.taskManager.service.project.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUserWhenUserDoesNotExist() {
        String firstName = "imisioluwanimi";
        String lastName = "Oke";
        String email = "imisi@test.com";
        String password = "12345";

        when(userRepository.userExists(email)).thenReturn(false);

        String result = userService.registerUser(firstName, lastName, email, password);

        verify(userRepository, times(1)).saveUser(any(UserImpl.class));
        assertEquals("User registered successfully.", result);
    }

    @Test
    void registerUserWhenUserExist() {

        String firstName = "imisioluwanimi";
        String lastName = "Oke";
        String email = "imisi@test.com";
        String password = "12345";

        when(userRepository.userExists(email)).thenReturn(true);

        String result = userService.registerUser(firstName, lastName, email, password);

        verify(userRepository, never()).saveUser(any(UserImpl.class));
        assertEquals("User with the email imisi@test.com already exist", result);
    }


    @Test
    void userLoginWhenUserNotFound() {
        String email = "imisi@test.com";
        String password = "12345";

        when(userRepository.findUserByEmail(email)).thenReturn(null);

        String result = userService.userLogin(email, password);

        verify(projectService, never()).setLoggedInUser(any(UserImpl.class));
        assertEquals("Login failed!", result);
    }

    @Test
    void userLoginWhenCredentialsAreValid() {
        String email = "imisi@test.com";
        String password = "12345";

        UserImpl mockUser = mock(UserImpl.class);
        when(userRepository.findUserByEmail(email)).thenReturn(mockUser);
        when(mockUser.checkPassword(password)).thenReturn(true);

        String result = userService.userLogin(email, password);

        verify(projectService, times(1)).setLoggedInUser(mockUser);
        assertEquals("Login successful!", result);
    }

    @Test
    void userLoginWhenCredentialsAreInvalid() {
        String email = "imsi@test.com";
        String password = "12345";

        UserImpl mockUser = mock(UserImpl.class);
        when(userRepository.findUserByEmail(email)).thenReturn(mockUser);
        when(mockUser.checkPassword(password)).thenReturn(false);

        String result = userService.userLogin(email, password);

        verify(projectService, never()).setLoggedInUser(mockUser);
        assertEquals("Login failed!", result);
    }
}