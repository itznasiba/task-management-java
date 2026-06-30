package com.example.service;

import com.example.model.Status;
import com.example.model.Task;
import com.example.model.User;
import com.example.repository.TaskRepositoryImpl;
import com.example.repository.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    private TaskRepositoryImpl taskRepository;
    private UserRepositoryImpl userRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepositoryImpl.class);
        userRepository = mock(UserRepositoryImpl.class);
        taskService = new TaskServiceImpl(taskRepository, userRepository);
    }

    @Test
    @DisplayName("Xəta: Başlığı boş olan task yadda saxlanıla bilməz")
    void should_ThrowException_When_TaskTitleIsEmpty() {
        Task invalidTask = new Task();
        invalidTask.setTitle("");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> taskService.save(invalidTask));
        assertEquals("Title can not be empty.", exception.getMessage());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Uğurlu Assign: Həm user, həm task mövcud olduqda assign edilməlidir")
    void should_AssignTask_When_BothUserAndTaskExist() {
        long userId = 10L;
        long taskId = 20L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(new Task()));
        when(userRepository.getUserById(userId)).thenReturn(Optional.of(new User()));

        assertDoesNotThrow(() -> taskService.assignTask(userId, taskId));
        verify(taskRepository, times(1)).assignTask(userId, taskId);
    }

    @Test
    @DisplayName("Xəta (Assign): User mövcud olmadıqda assign xətası atmalıdır")
    void should_ThrowException_OnAssign_When_UserDoesNotExist() {
        long userId = 888L;
        long taskId = 20L;

        when(userRepository.getUserById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> taskService.assignTask(userId, taskId));
        assertEquals("User you want to assign task is not found", exception.getMessage());
        verify(taskRepository, never()).assignTask(userId, taskId);
    }

    @Test
    @DisplayName("Uğurlu Status Yeniləmə: Task tapıldıqda onun statusunu DOING etməlidir")
    void should_UpdateStatus_When_TaskExists() {
        long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(new Task()));

        // Bayaq xəta verən hissəni Status.DOING ilə əvəzlədik!
        assertDoesNotThrow(() -> taskService.updateStatus(taskId, Status.DOING));

        verify(taskRepository, times(1)).updateStatus(taskId, Status.DOING);
    }
}