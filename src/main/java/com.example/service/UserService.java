package com.example.service;

import com.example.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void save(User user);
    Optional<User> getUserById(long id);
    List<User> findAll();
    void deleteById(long id);
    void updateUser(User user);
}
