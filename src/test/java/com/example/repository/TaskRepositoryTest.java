package com.example.repository;

import com.example.model.Status;
import com.example.model.Task;
import com.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TaskRepositoryTest {

    private JdbcTemplate jdbcTemplate;
    private TaskRepositoryImpl taskRepository;

    @BeforeEach
    void setUp() {
        // JdbcTemplate-i mock edirik və repozitoriyaya ötürürük
        jdbcTemplate = mock(JdbcTemplate.class);
        taskRepository = new TaskRepositoryImpl(jdbcTemplate);
    }

    @Test
    @DisplayName("Uğurlu Saxlama: save metodu düzgün SQL və parametrlərlə jdbcTemplate.update çağırmalıdır")
    void should_ExecuteInsertSql_When_SavingTask() {
        // GIVEN
        User user = new User();
        user.setId(5L);

        Task task = new Task();
        task.setTitle("Yeni Layihə");
        task.setDescription("Testləri yazmaq");
        task.setStatus(Status.NOT_STARTED);
        task.setAssignedUser(user);

        // WHEN
        taskRepository.save(task);

        // THEN
        String expectedSql = "INSERT INTO tasks(title,description,status,user_id)VALUES(?,?,?,?)";
        verify(jdbcTemplate, times(1)).update(
                eq(expectedSql),
                eq("Yeni Layihə"),
                eq("Testləri yazmaq"),
                eq("NOT_STARTED"),
                eq(5L)
        );
    }

    @Test
    @DisplayName("Uğurlu Tapılma: findById mövcud ID ilə tapılan taskı Optional daxilində qaytarmalıdır")
    void should_ReturnTask_When_TaskExistsById() {
        // GIVEN
        long taskId = 1L;
        Task mockTask = new Task();
        mockTask.setId(taskId);
        mockTask.setTitle("Oxumaq");
        mockTask.setStatus(Status.DOING);

        // query metodu çağırılarkən bizim yaratdığımız mock listini qaytarmasını simulyasiya edirik
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(taskId)))
                .thenReturn(Collections.singletonList(mockTask));

        // WHEN
        Optional<Task> result = taskRepository.findById(taskId);

        // THEN
        assertTrue(result.isPresent());
        assertEquals("Oxumaq", result.get().getTitle());
        assertEquals(Status.DOING, result.get().getStatus());
    }

    @Test
    @DisplayName("Uğurlu Tapılma (Boş): findById tapılmayan ID üçün Optional.empty() qaytarmalıdır")
    void should_ReturnEmptyOptional_When_TaskDoesNotExistById() {
        // GIVEN
        long taskId = 999L;
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(taskId)))
                .thenReturn(Collections.emptyList());

        // WHEN
        Optional<Task> result = taskRepository.findById(taskId);

        // THEN
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Uğurlu Status Yeniləmə: updateStatus düzgün SQL və status.name() ilə çağırılmalıdır")
    void should_ExecuteUpdateSql_When_UpdatingStatus() {
        // GIVEN
        long taskId = 10L;
        Status newStatus = Status.COMPLETED;

        // WHEN
        taskRepository.updateStatus(taskId, newStatus);

        // THEN
        String expectedSql = "UPDATE tasks SET status=? WHERE id =?";
        verify(jdbcTemplate, times(1)).update(expectedSql, "COMPLETED", taskId);
    }

    @Test
    @DisplayName("Uğurlu Assign: assignTask metodu user_id və task_id-ni bazada yeniləməlidir")
    void should_ExecuteUpdateSql_When_AssigningTaskToUser() {
        // GIVEN
        long userId = 3L;
        long taskId = 25L;

        // WHEN
        taskRepository.assignTask(userId, taskId);

        // THEN
        String expectedSql = "UPDATE tasks SET user_id=? WHERE id=?";
        verify(jdbcTemplate, times(1)).update(expectedSql, userId, taskId);
    }

    @Test
    @DisplayName("Uğurlu Silmə: deleteById metodu düzgün ID ilə silmə sorğusu icra etməlidir")
    void should_ExecuteDeleteSql_When_DeletingTask() {
        // GIVEN
        long taskId = 40L;

        // WHEN
        taskRepository.deleteById(taskId);

        // THEN
        String expectedSql = "DELETE FROM tasks WHERE id=?";
        verify(jdbcTemplate, times(1)).update(expectedSql, taskId);
    }
}