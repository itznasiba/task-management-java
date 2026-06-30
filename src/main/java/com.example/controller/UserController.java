package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){
        this.userService=userService;
    }
    @GetMapping
    public ResponseEntity<List<User>> findAll(){
        List<User> users=userService.findAll();
        return ResponseEntity.ok(users);
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id){
        Optional<User> userOptional=userService.getUserById(id);
        if(userOptional.isPresent()){
            User user=userOptional.get();
            return ResponseEntity.ok(user);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody User user){
        try{
            userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully.");
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PutMapping()
    public ResponseEntity<String> updateUser(@RequestBody User user){
        try{
            userService.updateUser(user);
            return ResponseEntity.status(HttpStatus.OK).body("User updated successfully.");
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") long id){
        try {
            userService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
