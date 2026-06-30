package com.example.repository;

import com.example.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> getUserById(long id);
    List<User> findAll();
    void save(User user);
    void updateUser(User user);
    void deleteById(long id);
}
