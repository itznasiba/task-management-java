package com.example.controller;

import com.example.model.Status;
import com.example.model.Task;
import com.example.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    private TaskService taskService;
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        taskService = mock(TaskService.class);
        taskController = new TaskController(taskService);
    }

    @Test
    @DisplayName("findAll() - Bütün tapşırıqları uğurla gətirməlidir")
    void should_ReturnAllTasks_When_FindAllCalled() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setStatus(Status.NOT_STARTED);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setStatus(Status.DOING);

        List<Task> mockTasks = Arrays.asList(task1, task2);
        when(taskService.findAll()).thenReturn(mockTasks);

        ResponseEntity<List<Task>> response = taskController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    @DisplayName("findById() - Tapşırıq mövcud olduqda tapşırığı qaytarmalıdır")
    void should_ReturnTask_When_TaskExists() {
        long taskId = 1L;
        Task mockTask = new Task();
        mockTask.setId(taskId);
        mockTask.setTitle("Test Task");
        mockTask.setStatus(Status.NOT_STARTED);

        when(taskService.findById(taskId)).thenReturn(Optional.of(mockTask));

        ResponseEntity<Task> response = taskController.findById(taskId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Task", response.getBody().getTitle());
    }

    @Test
    @DisplayName("findById() - Tapşırıq tapılmayanda HTTP 404 qaytarmalıdır")
    void should_ReturnNotFound_When_TaskDoesNotExist() {
        long taskId = 99L;
        when(taskService.findById(taskId)).thenReturn(Optional.empty());

        ResponseEntity<Task> response = taskController.findById(taskId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("save() - Uğurlu saxlama zamanı HTTP 200 qaytarmalıdır")
    void should_ReturnOk_When_TaskSavedSuccessfully() {
        Task task = new Task();
        task.setTitle("Valid Title");

        // DÜZƏLİŞ: void üçün mötərizə strukturu təmizləndi
        doNothing().when(taskService).save(task);

        ResponseEntity<String> response = taskController.save(task);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Task added successfully.", response.getBody());
    }

    @Test
    @DisplayName("save() - Başlıq boş olduqda HTTP 400 qaytarmalıdır")
    void should_ReturnBadRequest_When_TaskTitleIsEmpty() {
        Task invalidTask = new Task();
        invalidTask.setTitle("");

        // DÜZƏLİŞ: void üçün mötərizə strukturu təmizləndi
        doThrow(new RuntimeException("Title can not be empty."))
                .when(taskService).save(invalidTask);

        ResponseEntity<String> response = taskController.save(invalidTask);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Title can not be empty.", response.getBody());
    }

    @Test
    @DisplayName("deleteById() - Uğurlu silmə zamanı HTTP 200 qaytarmalıdır")
    void should_ReturnOk_When_TaskDeletedSuccessfully() {
        long taskId = 1L;

        // DÜZƏLİŞ: void üçün mötərizə strukturu təmizləndi
        doNothing().when(taskService).deleteById(taskId);

        ResponseEntity<String> response = taskController.deleteById(taskId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Task deleted successfully.", response.getBody());
    }

    @Test
    @DisplayName("deleteById() - Silinəcək tapşırıq tapılmayanda HTTP 404 qaytarmalıdır")
    void should_ReturnNotFound_When_DeletingNonExistingTask() {
        long taskId = 99L;

        // DÜZƏLİŞ: void üçün mötərizə strukturu təmizləndi
        doThrow(new RuntimeException("Task you want to delete is not found."))
                .when(taskService).deleteById(taskId);

        ResponseEntity<String> response = taskController.deleteById(taskId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Task you want to delete is not found.", response.getBody());
    }

    @Test
    @DisplayName("getTaskByUserId() - İstifadəçinin tapşırıqlarını gətirməlidir")
    void should_ReturnUserTasks_When_UserExists() {
        long userId = 5L;
        Task userTask = new Task();
        userTask.setId(1L);
        userTask.setTitle("User Task");
        userTask.setStatus(Status.DOING);

        List<Task> mockTasks = Collections.singletonList(userTask);
        when(taskService.getTasksByUserId(userId)).thenReturn(mockTasks);

        ResponseEntity<?> response = taskController.getTaskByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTasks, response.getBody());
    }

    @Test
    @DisplayName("getTaskByUserId() - İstifadəçi tapılmayanda HTTP 404 qaytarmalıdır")
    void should_ReturnNotFound_When_UserDoesNotExistForTasks() {
        long userId = 99L;
        when(taskService.getTasksByUserId(userId)).thenThrow(new RuntimeException("User is not found."));

        ResponseEntity<?> response = taskController.getTaskByUserId(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User is not found.", response.getBody());
    }

    @Test
    @DisplayName("updateStatus() - Status uğurla yenilənəndə HTTP 200 qaytarmalıdır")
    void should_ReturnOk_When_StatusUpdatedSuccessfully() {
        long taskId = 1L;
        Status newStatus = Status.COMPLETED;

        // DÜZƏLİŞ: void üçün mötərizə strukturu təmizləndi
        doNothing().when(taskService).updateStatus(taskId, newStatus);

        ResponseEntity<String> response = taskController.updateStatus(taskId, newStatus);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Status updated successfully.", response.getBody());
    }

    @Test
    @DisplayName("updateStatus() - Tapşırıq tapılmayanda HTTP 404 qaytarmalıdır")
    void should_ReturnNotFound_When_UpdatingStatusOfNonExistingTask() {
        long taskId = 99L;
        Status newStatus = Status.COMPLETED;

        // DÜZƏLİŞ: void üçün mötərizə strukturu təmizləndi
        doThrow(new RuntimeException("Task you want to update is not found"))
                .when(taskService).updateStatus(taskId, newStatus);

        ResponseEntity<String> response = taskController.updateStatus(taskId, newStatus);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Task you want to update is not found", response.getBody());
    }

    @Test
    @DisplayName("assignTask() - Tapşırıq uğurla təyin edildikdə HTTP 200 qaytarmalıdır")
    void should_ReturnOk_When_TaskAssignedSuccessfully() {
        long userId = 2L;
        long taskId = 10L;

        // DÜZƏLİŞ: void üçün mötərizə strukturu təmizləndi
        doNothing().when(taskService).assignTask(userId, taskId);

        ResponseEntity<String> response = taskController.assignTask(userId, taskId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Task assigned to user successfully", response.getBody());
    }

    @Test
    @DisplayName("assignTask() - Təyin zamanı istifadəçi tapılmayanda HTTP 404 qaytarmalıdır")
    void should_ReturnNotFound_When_AssigningFailsDueToMissingUser() {
        long userId = 99L;
        long taskId = 10L;

        // DÜZƏLİŞ: void üçün mötərizə strukturu təmizləndi
        doThrow(new RuntimeException("User you want to assign task is not found"))
                .when(taskService).assignTask(userId, taskId);

        ResponseEntity<String> response = taskController.assignTask(userId, taskId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User you want to assign task is not found", response.getBody());
    }

    @Test
    @DisplayName("assignTask() - Təyin zamanı tapşırıq tapılmayanda HTTP 404 qaytarmalıdır")
    void should_ReturnNotFound_When_AssigningFailsDueToMissingTask() {
        long userId = 2L;
        long taskId = 99L;

        // DÜZƏLİŞ: void üçün mötərizə strukturu təmizləndi
        doThrow(new RuntimeException("Task you want to assign is not found"))
                .when(taskService).assignTask(userId, taskId);

        ResponseEntity<String> response = taskController.assignTask(userId, taskId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Task you want to assign is not found", response.getBody());
    }
}