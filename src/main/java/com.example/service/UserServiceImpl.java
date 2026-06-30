package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepositoryImpl userRepository;

    public UserServiceImpl(UserRepositoryImpl userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public void save(User user){
        boolean emailExists=userRepository.findAll().stream()
                .anyMatch(u->u.getEmail().equalsIgnoreCase(user.getEmail()));
        if(emailExists){
            throw new RuntimeException("The account with this email exists.");
        }
        userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(long id){
        Optional<User> user=userRepository.getUserById(id);
        if(user.isEmpty()){
            System.out.println("LOG: " + id + " ID-li istifadəçi tapılmadı.");
        }
        return user;
    }

    @Override
    public List<User> findAll(){

        return userRepository.findAll();
    }

    @Override
    public void updateUser(User user){
        if(userRepository.getUserById(user.getId()).isEmpty()){
            throw new RuntimeException("User you want to update does not exist");
        }
        userRepository.updateUser(user);
    }

    @Override
    public void deleteById(long id){
        if(userRepository.getUserById(id).isEmpty()){
            throw new RuntimeException("User you want to delete does not exist");
        }
        userRepository.deleteById(id);
    }
}
