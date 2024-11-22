package com.kunmi.taskManager.service.user;

import com.kunmi.taskManager.repository.userRepo.UserRepository;
import com.kunmi.taskManager.service.project.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterUserWhenUserDoesNotExist() {
        String firstName = "imisioluwanimi";
        String lastName = "Oke";
        String email = "imisi@test.com";
        String password = "12345";

        when(userRepository.userExists(email)).thenReturn(false);

        String result = userService.registerUser(firstName, lastName, email, password);

        verify(userRepository, times(1)).saveUser(any(User.class));
        assertEquals("User registered successfully.", result);
    }

    @Test
    void shouldRegisterUserWhenUserExist() {

        String firstName = "imisioluwanimi";
        String lastName = "Oke";
        String email = "imisi@test.com";
        String password = "12345";

        when(userRepository.userExists(email)).thenReturn(true);

        String result = userService.registerUser(firstName, lastName, email, password);

        verify(userRepository, never()).saveUser(any(User.class));
        assertEquals("User with the email imisi@test.com already exist", result);
    }


    @Test
    void shouldNotLoginWhenUserNotFound() {
        String email = "imisi@test.com";
        String password = "12345";

        when(userRepository.findUserByEmail(email)).thenReturn(null);

        String result = userService.userLogin(email, password);

        assertNull(UserContext.getCurrentUser());
        assertEquals("Login failed!", result);
    }

    @Test
    void shouldLoginWhenCredentialsAreValid() {
        String email = "imisi@test.com";
        String password = "12345";

        User mockUser = mock(User.class);
        UserServiceImpl mockUserService = mock(UserServiceImpl.class);

        when(userRepository.findUserByEmail(email)).thenReturn(mockUser);
        when(mockUserService.checkPassword(password)).thenReturn(true);

        String result = userService.userLogin(email, password);

        assertEquals(mockUser, UserContext.getCurrentUser());
        assertEquals("Login successful!", result);
    }

    @Test
    void shouldNotLoginWhenCredentialsAreInvalid() {
        String email = "imsi@test.com";
        String password = "12345";

        User mockUser = mock(User.class);
        when(userRepository.findUserByEmail(email)).thenReturn(mockUser);

        UserServiceImpl userServiceSpy = spy(new UserServiceImpl(userRepository));
        doReturn(false).when(userServiceSpy).checkPassword(password);

        String result = userServiceSpy.userLogin(email, password);

        assertNull(UserContext.getCurrentUser());
        assertEquals("Login failed!", result);
    }
}