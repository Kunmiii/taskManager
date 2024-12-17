package com.kunmi.taskManager.service.task;

import com.kunmi.taskManager.repository.projectRepo.ProjectRepository;
import com.kunmi.taskManager.repository.taskRepo.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setup() {

    }

    @Test
    void shouldCreateTaskWhenProjectIdIsNotNull() {
        String projectId = "1";
        String taskName = "Task Demo 1";
        LocalDateTime createDate = LocalDateTime.now();

        when(projectRepository.existsById(projectId)).thenReturn(true);
        taskService.create(taskName, projectId, createDate);

        verify(taskRepository, times(1)).addTask(eq(projectId), any(Task.class));
    }

    @Test
    void shouldNotCreateTaskWhenProjectIdIsNull() {
        String projectId = "null";
        String taskName = "Task Demo 1";
        LocalDateTime createDate = LocalDateTime.now();

        when(projectRepository.existsById(projectId)).thenThrow(new IllegalArgumentException("Project ID must not be null or blank"));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.create(taskName, projectId, createDate);
        });

        assertEquals("Project ID must not be null or blank", exception.getMessage());
        verifyNoInteractions(taskRepository);
    }

    @Test
    void shouldUpdateTaskWhenProjectIdIsNotNull() {
        String projectId = "123";
        String taskId = "2";
        LocalDateTime createDate = LocalDateTime.now();
        String newTaskName = "UpdatedTaskName";
        String oldTaskName = "OldTaskName";

        Task existingTask = new Task(oldTaskName, createDate, projectId);

        when(projectRepository.existsById(projectId)).thenReturn(true);
        when(taskRepository.getTask(taskId, projectId)).thenReturn(existingTask);

        taskService.update(projectId, taskId, newTaskName);

        assertEquals(newTaskName, existingTask.getName());

        verify(projectRepository, times(1)).existsById(projectId);
        verify(taskRepository, times(1)).getTask(taskId, projectId);
    }

    @Test
    void shouldNotUpdateTaskWhenProjectIdIsInvalid() {
        String projectId = "123";
        String taskId = "2";
        String newTaskName = "UpdatedTaskName";

        when(projectRepository.existsById(projectId)).thenReturn(false);

        taskService.update(projectId, taskId, newTaskName);

        verify(projectRepository, times(1)).existsById(projectId);
        verify(taskRepository, never()).getTask(anyString(), anyString());
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void shouldNotUpdateTaskWhenTaskIdIsInvalid() {
        String projectId = "123";
        String taskId = "2";
        String newTaskName = "UpdatedTaskName";

        when(projectRepository.existsById(projectId)).thenReturn(true);
        when(taskRepository.getTask(taskId, projectId)).thenReturn(null);

        taskService.update(projectId, taskId, newTaskName);

        verify(projectRepository, times(1)).existsById(projectId);
        verify(taskRepository, times(1)).getTask(taskId, projectId);
        verifyNoMoreInteractions(taskRepository);
    }

    @Test
    void shouldFindAllTasksWhenProjectIdIsValid() {
        String projectId = "123";
        Task task1 = new Task("Task 1", LocalDateTime.now(), projectId);
        Task task2 = new Task("Task 2", LocalDateTime.now(), projectId);

        List<Task> tasks = List.of(task1, task2);

        when(projectRepository.existsById(projectId)).thenReturn(true);
        when(taskRepository.getProjectTasks(projectId)).thenReturn(tasks);

        taskService.findAll(projectId);

        verify(taskRepository, times(1)).getProjectTasks(eq(projectId));
    }

    @Test
    void shouldNotFindAllTasksWhenProjectIdIsNull() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.findAll(null);
        });

       assertEquals("projectId must be provided", exception.getMessage());

        verifyNoInteractions(taskRepository);
    }

    @Test
    void shouldDeleteTaskWhenProjectIdIsValid() {
        String projectId = "123";
        String taskId = "2";

        when(projectRepository.existsById(projectId)).thenReturn(true);

        taskService.delete(projectId, taskId);

        verify(projectRepository, times(1)).existsById(eq(projectId));
        verify(taskRepository, times(1)).removeTask(taskId, projectId);
        verifyNoMoreInteractions(taskRepository);

    }

    @Test
    void shouldNotDeleteTaskWhenProjectIdIsInvalid() {
        String projectId = "123";
        String taskId = "1";

        when(projectRepository.existsById(projectId)).thenReturn(false);

        taskService.delete(projectId, taskId);

        verify(projectRepository, times(1)).existsById(projectId);
        verify(taskRepository, never()).removeTask(anyString(), anyString());

    }
}