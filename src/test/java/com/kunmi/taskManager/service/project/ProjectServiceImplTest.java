package com.kunmi.taskManager.service.project;

import com.kunmi.taskManager.repository.projectRepo.ProjectRepository;
import com.kunmi.taskManager.service.user.UserImpl;
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

class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserImpl mockUser;

    private ProjectServiceImpl projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = mock(UserImpl.class);

        projectService = new ProjectServiceImpl(projectRepository, mockUser);
        projectService.setLoggedInUser(mockUser);

        when(mockUser.getId()).thenReturn("mockUserId");
    }

    @Test
    void constructorShouldInitializeRepositoryAndUser() {
        projectService = new ProjectServiceImpl(projectRepository, mockUser);
        assertEquals(mockUser, projectService.getLoggedInUser());
    }

    @Test
    void createShouldAddProjectWhenUserIsLoggedIn() {
        String projectName = "Project X";
        LocalDateTime createDate = LocalDateTime.now();

        projectService.create(projectName, createDate);
        verify(projectRepository, times(1)).addProject(eq("mockUserId"), any(Project.class));
    }

    @Test
    void createShouldNotAddProjectWhenUserIsNotLoggedIn() {
        String projectName = "Project X";
        LocalDateTime createDate = LocalDateTime.now();
        projectService.setLoggedInUser(null);

        projectService.create(projectName, createDate);
        verify(projectRepository, never()).addProject(any(), any());
    }

    @Test
    void updateShouldUpdateProjectWhenProjectExist() {
        String newProjectName = "ProjectX version II";
        String projectId = "1";

        Project existingProject = new Project("Project X", LocalDateTime.now(), "mockUserId");

        when(projectRepository.getProject(projectId, "mockUserId")).thenReturn(existingProject);

        projectService.update(projectId, newProjectName);

        verify(projectRepository, times(1)).addProject(eq("mockUserId"), any(Project.class));
        assertEquals(newProjectName, existingProject.getName());
    }

    @Test
    void updateShouldNotUpdateProjectWhenProjectDoesNotExist() {
        String newProjectName = "ProjectX version II";
        String projectId = "1";

        when(projectRepository.getProject(projectId, mockUser.getId())).thenReturn(null);

        projectService.update(projectId, newProjectName);

        verify(projectRepository, never()).addProject(any(), any(Project.class));
    }

    @Test
    void updateShouldNotUpdateProjectWhenUserAndProjectDoesNotExist() {
        String newProjectName = "ProjectX version II";
        String projectId = "1";

        projectService.setLoggedInUser(null);

        projectService.update(projectId, newProjectName);

        verify(projectRepository, never()).getProject(anyString(), anyString());
        verify(projectRepository, never()).addProject(anyString(), any(Project.class));
    }

    @Test
    void viewShouldPrintProjectsWhenUserIsLoggedIn() {
        String projectName = "Project X";
        LocalDateTime createDate = LocalDateTime.now();

        Project project = new Project(projectName, createDate, "mockUserId");

        when(projectRepository.getUserProjects(project.getUserId(), project.getId())).thenReturn(List.of(project));

        projectService.view(project.getId());

        verify(projectRepository, times(1)).getUserProjects(project.getUserId(), project.getId());
    }

    @Test
    void viewShouldPrintMessageWhenUserIsNotLoggedIn() {
        String projectId = "1";
        projectService.setLoggedInUser(null);

        projectService.view(projectId);

        verify(projectRepository, never()).getUserProjects(anyString(), anyString());
    }

    @Test
    void viewShouldPrintMessageWhenProjectDoesNotExist() {
        String projectId = "1";

        projectService.view(projectId);

        verify(projectRepository, times(1)).getUserProjects(mockUser.getId(), projectId);
    }

    @Test
    void viewShouldPrintExceptionWhenProjectIsInvalid() {
        String invalidProjectId = null;

        projectService.view(invalidProjectId);

        verify(projectRepository, never()).getUserProjects(anyString(), anyString());
    }

    @Test
    void delete() {
    }

}