package com.kunmi.taskManager.service.project;

import com.kunmi.taskManager.repository.projectRepo.ProjectRepository;
import com.kunmi.taskManager.service.user.User;
import com.kunmi.taskManager.service.user.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private User mockUser;

    private ProjectServiceImpl projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = mock(User.class);

        projectService = new ProjectServiceImpl(projectRepository);

        UserContext.setCurrentUser(mockUser);

        when(mockUser.getId()).thenReturn("mockUserId");
    }

    @Test
    void shouldAddProjectWhenUserIsLoggedIn() {
        String projectName = "Project X";
        LocalDateTime createDate = LocalDateTime.now();

        projectService.create(projectName, createDate);
        verify(projectRepository, times(1)).addProject(eq("mockUserId"), any(Project.class));
    }

    @Test
    void shouldNotAddProjectWhenUserIsNotLoggedIn() {
        String projectName = "Project X";
        LocalDateTime createDate = LocalDateTime.now();
        UserContext.setCurrentUser(null);

        projectService.create(projectName, createDate);
        verify(projectRepository, never()).addProject(any(), any());
    }

    @Test
    void shouldUpdateProjectWhenProjectExist() {
        String newProjectName = "ProjectX version II";
        String projectId = "1";

        Project existingProject = new Project("Project X", LocalDateTime.now(), "mockUserId");

        when(projectRepository.getProject(projectId, "mockUserId")).thenReturn(existingProject);

        projectService.update(projectId, newProjectName);

        verify(projectRepository, times(1)).addProject(eq("mockUserId"), any(Project.class));
        assertEquals(newProjectName, existingProject.getName());
    }

    @Test
    void shouldNotUpdateProjectWhenProjectDoesNotExist() {
        String newProjectName = "ProjectX version II";
        String projectId = "1";

        when(projectRepository.getProject(projectId, mockUser.getId())).thenReturn(null);

        projectService.update(projectId, newProjectName);

        verify(projectRepository, never()).addProject(any(), any(Project.class));
    }

    @Test
    void shouldNotUpdateProjectWhenUserAndProjectDoesNotExist() {
        String newProjectName = "ProjectX version II";
        String projectId = "1";

        UserContext.setCurrentUser(null);

        projectService.update(projectId, newProjectName);

        verify(projectRepository, never()).getProject(anyString(), anyString());
        verify(projectRepository, never()).addProject(anyString(), any(Project.class));
    }

    @Test
    void shouldPrintProjectsWhenUserIsLoggedIn() {
        String projectName = "Project X";
        LocalDateTime createDate = LocalDateTime.now();

        Project project = new Project(projectName, createDate, "mockUserId");

        when(projectRepository.getUserProjects(project.getUserId(), project.getId())).thenReturn(List.of(project));

        projectService.findAll(project.getId());

        verify(projectRepository, times(1)).getUserProjects(project.getUserId(), project.getId());
    }

    @Test
    void shouldPrintMessageWhenUserIsNotLoggedIn() {
        String projectId = "1";
        UserContext.setCurrentUser(null);
        projectService.findAll(projectId);

        verify(projectRepository, never()).getUserProjects(anyString(), anyString());
    }

    @Test
    void shouldPrintMessageWhenProjectDoesNotExist() {
        String projectId = "1";

        projectService.findAll(projectId);

        verify(projectRepository, times(1)).getUserProjects(mockUser.getId(), projectId);
    }

    @Test
    void shouldPrintExceptionWhenProjectIsInvalid() {
        String invalidProjectId = null;

        projectService.findAll(invalidProjectId);

        verify(projectRepository, never()).getUserProjects(anyString(), anyString());
    }

    @Test
    void delete() {
    }

}