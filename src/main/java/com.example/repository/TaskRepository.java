package com.example.repository;

import com.example.model.Status;
import com.example.model.Task;

import java.util.List;
import java.util.Optional;
public interface TaskRepository {



        void save(Task task);
        Optional<Task> findById(long id);
        List<Task> findAll();
        List<Task> getTasksByUserId(long userId);
        void deleteById(long id);
        void updateStatus(long id, Status status);
        void assignTask(long userId, long taskId);
}
