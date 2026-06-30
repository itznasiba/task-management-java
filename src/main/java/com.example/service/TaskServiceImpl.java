package com.example.service;

import com.example.model.Status;
import com.example.model.Task;
import com.example.repository.TaskRepositoryImpl;
import com.example.repository.UserRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepositoryImpl taskRepository;
    private final UserRepositoryImpl userRepository;
    public TaskServiceImpl(TaskRepositoryImpl taskRepository, UserRepositoryImpl userRepository){
        this.taskRepository=taskRepository;
        this.userRepository=userRepository;
    }

    @Override
    public void save(Task task){
        if(task.getTitle()==null || task.getTitle().isEmpty()){
            throw new RuntimeException("Title can not be empty.");
        }
        taskRepository.save(task);
    }

    @Override
    public Optional<Task> findById(long id){
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> findAll(){

        return taskRepository.findAll();
    }

    @Override
    public List<Task> getTasksByUserId(long userId) {
        if (userRepository.getUserById(userId).isEmpty()){
            throw new RuntimeException("User is not found.");
        }
        return taskRepository.getTasksByUserId(userId);
    }
    @Override
    public void deleteById(long id) {
        if(taskRepository.findById(id).isEmpty()){
            throw new RuntimeException("Task you want to delete is not found.");
        }
        taskRepository.deleteById(id);
    }

    @Override
    public void updateStatus(long id, Status status){
        if(taskRepository.findById(id).isEmpty()){
            throw new RuntimeException("Task you want to update is not found");
        }
        taskRepository.updateStatus(id,status);
    }
    @Override
    public void assignTask(long userId, long taskId){
        if(userRepository.getUserById(userId).isEmpty()){
            throw new RuntimeException("User you want to assign task is not found");
        }
        if(taskRepository.findById(taskId).isEmpty()){
            throw new RuntimeException("Task you want to assign is not found");
        }
        taskRepository.assignTask(userId,taskId);
    }

}
