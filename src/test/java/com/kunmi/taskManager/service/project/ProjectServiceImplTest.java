package com.kunmi.taskManager.service.project;

import com.kunmi.taskManager.exceptions.ProjectNotFoundException;
import com.kunmi.taskManager.models.Project;
import com.kunmi.taskManager.repository.projectRepo.ProjectRepository;
import com.kunmi.taskManager.models.User;
import com.kunmi.taskManager.service.user.UserContext;
import com.kunmi.taskManager.utils.validation.ValidationUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private User loggedInUser;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateProjectSuccessfully() {
        String projectID = "1";
        String projectName = "Test Project";
        LocalDateTime createDate = LocalDateTime.now();

        try (MockedStatic<UserContext> mockedUserContext = mockStatic(UserContext.class);
             MockedStatic<ValidationUtils> mockedValidationUtils = mockStatic(ValidationUtils.class)) {

            // Mock UserContext to return the logged-in user
            mockedUserContext.when(UserContext::getCurrentUser).thenReturn(loggedInUser);

            // Mock loggedInUser methods
            when(loggedInUser.getId()).thenReturn("1");

            // Mock ValidationUtils methods
            mockedValidationUtils.when(() -> ValidationUtils.validateNotNull(any(), anyString())).thenAnswer(invocation -> null);
            mockedValidationUtils.when(() -> ValidationUtils.validateInputs(anyString(), anyString())).thenAnswer(invocation -> null);

            // Call the create method
            projectService.create(projectName, createDate);

            // Verify the repository interaction
            verify(projectRepository, times(1)).saveProject(any(Project.class));
        }
    }

    @Test
    void shouldNotCreateProjectWhenUserIsNull() {
        String projectName = "Test Project";
        LocalDateTime createDate = LocalDateTime.now();

        try (MockedStatic<UserContext> mockedUserContext = mockStatic(UserContext.class)) {
            mockedUserContext.when(UserContext::getCurrentUser).thenReturn(null);

            NullPointerException exception = assertThrows(NullPointerException.class, () -> {
                projectService.create(projectName, createDate);
            });

            assertEquals("Logged-in user must not be null", exception.getMessage());
        }
    }

    @Test
    void shouldNotCreateProjectWhenProjectNameIsInvalid() {
        String projectName = "Test Project";
        LocalDateTime createDate = LocalDateTime.now();

        try (MockedStatic<UserContext> mockedUserContext = mockStatic(UserContext.class)) {
            mockedUserContext.when(UserContext::getCurrentUser).thenReturn(loggedInUser);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                projectService.create("", createDate);
            });

            assertEquals("ProjectName must be provided", exception.getMessage());
        }
    }

    @Test
    void shouldUpdateProjectSuccessfully() {
        String projectID = "1";
        String newProjectName = "New Project";

        try(MockedStatic<UserContext> mockedUserContext = mockStatic(UserContext.class);
            MockedStatic<ValidationUtils> mockedValidationUtils = mockStatic(ValidationUtils.class)) {

            mockedUserContext.when(UserContext::getCurrentUser).thenReturn(loggedInUser);
            mockedValidationUtils.when(() -> ValidationUtils.validateInputs(eq(projectID), eq("ProjectID")))
                    .thenAnswer(invocation -> null);
            mockedValidationUtils.when(() -> ValidationUtils.validateInputs(eq(newProjectName), eq("NewProjectName")))
                    .thenAnswer(invocation -> null);
            mockedValidationUtils.when(() -> ValidationUtils.validateNotNull(any(), anyString()))
                    .thenAnswer(invocation -> null);
            
            Project project = new Project("Old Project", LocalDateTime.now(), loggedInUser);
            when(projectRepository.getProject(projectID, loggedInUser.getId())).thenReturn(project);

            projectService.update(projectID, newProjectName);

            assertEquals("Project name should be updated to the new value", newProjectName, project.getName());
            verify(projectRepository, times(1)).updateProject(any(Project.class));


        }
    }

    @Test
    void shouldThrowProjectNotFoundExceptionWhenProjectIdInvalid() {
        String projectId = "1";
        String newProjectName = "Updated Project";

        try (MockedStatic<UserContext> mockedUserContext = mockStatic(UserContext.class)) {
            mockedUserContext.when(UserContext::getCurrentUser).thenReturn(loggedInUser);

            when(loggedInUser.getId()).thenReturn("123");
//            when(projectRepository.getProject(projectId, "123"))
//                    .thenThrow(new ProjectNotFoundException("Project not found"));

            ProjectNotFoundException exception = assertThrows(ProjectNotFoundException.class, () -> {
                projectService.update(projectId, newProjectName);
            });

            assertEquals("No projects found for user.123", exception.getMessage());
        }
    }

    @Test
    void shouldFindAllProjectWithValidUser() {
         try (MockedStatic<UserContext> mockedUserContext = mockStatic(UserContext.class)) {
             mockedUserContext.when(UserContext::getCurrentUser).thenReturn(loggedInUser);

             when(loggedInUser.getId()).thenReturn(123L);
             Project project = new Project("Test Project", LocalDateTime.now(), loggedInUser);
             when(projectRepository.getUserProjects(123L))
                     .thenReturn(Collections.singletonList(project));

             List<Project> projects = projectService.findAll();

             assertEquals(1, projects.size());
             assertEquals("Test Project", projects.getFirst().getName());
         }
    }

    @Test
    void shouldReturnEmptyListWhenThereIsNoProject() {
        
        try (MockedStatic<UserContext> mockedUserContext = mockStatic(UserContext.class)) {
                mockedUserContext.when(UserContext::getCurrentUser).thenReturn(loggedInUser);

                when(loggedInUser.getId()).thenReturn("123");
                when(projectRepository.getUserProjects("123")).thenReturn(Collections.emptyList());

                List<Project> projects = projectService.findAll();

                assertTrue(projects.isEmpty());
        }
    }

    @Test
    void shouldReturnEmptyListWhenUserLoggedInIsNull() {
        try (MockedStatic<UserContext> mockedUserContext = mockStatic(UserContext.class)) {
            mockedUserContext.when(UserContext::getCurrentUser).thenReturn(loggedInUser);

            when(loggedInUser.getId()).thenReturn(null);

            List<Project> projects = projectService.findAll();

            assertTrue(projects.isEmpty());
        }
    }

    @SneakyThrows
    @Test
    void shouldDeleteProjectSuccessfully() {
        Long projectId = 123L;

        try (MockedStatic<UserContext> mockedUserContext = mockStatic(UserContext.class)) {
            mockedUserContext.when(UserContext::getCurrentUser).thenReturn(loggedInUser);
            when(loggedInUser.getId()).thenReturn(1L);

            Project project = new Project("Test Project", LocalDateTime.now(), loggedInUser);
            when(projectRepository.getProject(projectId, 123L)).thenReturn(project);

            projectService.delete(projectId);

            verify(projectRepository).removeProject(projectId, 1L);
        }
    }

    @SneakyThrows
    @Test
    void shouldThrowProjectNotFoundExceptionWhenProjectNotFound() {
        String projectId = "123";

        try (MockedStatic<UserContext> mockedUserContext = mockStatic(UserContext.class)) {
            mockedUserContext.when(UserContext::getCurrentUser).thenReturn(loggedInUser);
            when(loggedInUser.getId()).thenReturn("1");

            ProjectNotFoundException exception = assertThrows(ProjectNotFoundException.class, () -> {
                projectService.delete(projectId);
            });

           assertEquals("No projects found for user.1" , exception.getMessage());
        }
    }

    @Test
    void shouldThrowNullPointerExceptionWhenUserIsNull() {
        String projectId = "123";

        try (MockedStatic<UserContext> mockedUserContext = mockStatic(UserContext.class))
        {
            mockedUserContext.when(UserContext::getCurrentUser).thenReturn(null);

            NullPointerException exception = assertThrows(NullPointerException.class, () -> {
                projectService.delete(projectId);
            });

            assertEquals("Logged-in user must not be null", exception.getMessage());
        }

    }
}