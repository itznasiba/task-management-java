package com.example.service;

import com.example.model.Status;
import com.example.model.Task;
import com.example.model.User;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    void save(Task task);
    Optional<Task> findById(long id);
    List<Task> findAll();
    void updateStatus(long id, Status status);
    void assignTask(long userId,long taskId);
    List<Task> getTasksByUserId(long userId);
    void deleteById(long id);
}
