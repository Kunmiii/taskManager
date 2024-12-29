package com.kunmi.taskManager.service.user;

import com.kunmi.taskManager.exceptions.UserAlreadyExistsException;
import com.kunmi.taskManager.exceptions.UserNotFoundException;
import com.kunmi.taskManager.repository.userRepo.UserRepository;
import com.kunmi.taskManager.utils.validation.ValidationUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private MockedStatic<ValidationUtils> validationUtilsMock;
    private MockedStatic<UserContext> userContextMock;

    @BeforeEach
    void setUp() {
        validationUtilsMock = mockStatic(ValidationUtils.class);
        userContextMock = mockStatic(UserContext.class);
    }

    @AfterEach
    void tearDown() {
        validationUtilsMock.close();
        userContextMock.close();
    }

    @Test
    void shouldRegisterUserWithValidInputs() {
        String firstName = "imisioluwanimi";
        String lastName = "Oke";
        String email = "imisi@test.com";
        String password = "12345";

        doNothing().when(userRepository).saveUser(any());
        validationUtilsMock.when(() ->ValidationUtils.validateInputs(anyString(), anyString()))
                .thenAnswer(invocation -> null);
        validationUtilsMock.when(() -> ValidationUtils.validateUserExists(anyBoolean(), anyString()))
                .thenAnswer(invocation -> null);

        String result = userService.registerUser(firstName, lastName, email, password);

        assertEquals("User registered successfully", result);
        verify(userRepository, times(1)).saveUser(any());
    }

    @Test
    void shouldReturnValidationErrorWhenInputsAreInvalid() {
        String firstName = "";
        String lastName = "Oke";
        String email = "imisi@test.com";
        String password = "12345";

        validationUtilsMock.when(() -> ValidationUtils.validateInputs(eq(firstName), anyString()))
                .thenThrow(new IllegalArgumentException("Name must be provided"));

        String result = userService.registerUser(firstName, lastName, email, password);

        assertEquals("Name must be provided", result);

        verify(userRepository, never()).saveUser(any());
    }

    @Test
    void shouldReturnErrorWhenUserAlreadyExists() {
        String firstName = "Imisioluwanimi";
        String lastName = "Oke";
        String email = "imisi@test.com";
        String password = "12345";

        validationUtilsMock.when(() -> ValidationUtils.validateInputs(anyString(), anyString()))
                        .thenAnswer(invocation -> null);
        validationUtilsMock.when(() -> ValidationUtils.validateUserExists(anyBoolean(), anyString()))
                        .thenThrow(new UserAlreadyExistsException("User with email imisi@test.com already exist"));

        String result = userService.registerUser(firstName, lastName, email, password);

        assertEquals("User with email imisi@test.com already exist", result);
        verify(userRepository, never()).saveUser(any());
    }

    @Test
    void shouldThrownRuntimeExceptionForUnexpectedError() {
        String firstName = "Imisioluwanimi";
        String lastName = "Oke";
        String email = "imisi@test.com";
        String password = "12345";

        validationUtilsMock.when(() -> ValidationUtils.validateInputs(anyString(), anyString()))
                .thenAnswer(invocation -> null);
        validationUtilsMock.when(() -> ValidationUtils.validateUserExists(anyBoolean(), anyString()))
                .thenAnswer(invocation -> null);

        doThrow(new RuntimeException("Database Error")).when(userRepository).saveUser(any());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(firstName,lastName,password, email);
        });

        assertEquals("An unexpected error occurred during registration", exception.getMessage());

        verify(userRepository, times(1)).saveUser(any());

    }

    @Test
    void shouldLoginUserSuccessfully() {
        String email = "imisi@test.com";
        String password = "12345";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User mockUser = new User("Imisioluwanimi", "Oke", hashedPassword, email);

        when(userRepository.getUser(email)).thenReturn(mockUser);
        validationUtilsMock.when(() -> ValidationUtils.validateInputs(email, "email"))
                .thenAnswer(invocation -> null);
        validationUtilsMock.when(() -> ValidationUtils.validateInputs(password, "password"))
                .thenAnswer(invocation -> null);
        validationUtilsMock.when(() -> ValidationUtils.validateUserLogin(mockUser))
                .thenAnswer(invocation -> null);

        String result = userService.userLogin(email, password);

        assertEquals("Login successful", result);
        userContextMock.verify(() -> UserContext.setCurrentUser(mockUser));
    }

    @Test
    void shouldReturnValidationErrorForEmptyMail() {
        String email = "";
        String password = "12345";

        validationUtilsMock.when(() -> ValidationUtils.validateInputs(email, "Email"))
                .thenThrow(new IllegalArgumentException("Email must be provided"));

        String result = userService.userLogin(email, password);

        assertEquals("Email must be provided", result);
        verify(userRepository, never()).getUser(anyString());

    }

    @Test
    void shouldReturnErrorWhenUserNotFound() {
        String email = "fakeuser@test.com";
        String password = "12345";

        when(userRepository.getUser(email)).thenReturn(null);
        validationUtilsMock.when(() -> ValidationUtils.validateInputs(anyString(), anyString()))
                .thenAnswer(invocation -> null);
        validationUtilsMock.when(() -> ValidationUtils.validateUserLogin(null))
                .thenThrow(new UserNotFoundException("User does not have an account"));

        String result = userService.userLogin(email, password);

        assertEquals("User does not have an account", result);
        verify(userRepository, times(1)).getUser(email);
    }

    @Test
    void shouldReturnErrorForInvalidPassword() {
        String email = "imisi@test.com";
        String password = "54321";
        String hashedPassword = BCrypt.hashpw("12345", BCrypt.gensalt());
        User mockUser = new User("Imisioluwanimi", "Oke", hashedPassword, email);

        when(userRepository.getUser(email)).thenReturn(mockUser);
        validationUtilsMock.when(() -> ValidationUtils.validateInputs(anyString(), anyString()))
                .thenAnswer(invocation -> null);
        validationUtilsMock.when(() -> ValidationUtils.validateUserLogin(mockUser))
                .thenAnswer(invocation -> null);
        //doReturn(false).when(userService).checkPassword(password, hashedPassword);

        String result = userService.userLogin(email, password);

        assertEquals("Invalid email or password", result);
    }
}