package com.example.controller;

import com.example.model.Role;
import com.example.model.User;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User mockUser;

    @BeforeEach
    void setUp() {
        // Sizin User modelinizə uyğun olaraq obyekt yaradılır (status sahəsi burdan çıxarıldı)
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setName("Ali");
        mockUser.setEmail("ali@example.com");
        mockUser.setRole(Role.USER); // ADMIN, USER, MANAGER enumu
    }

    // --- GET /api/users ---
    @Test
    void findAll_ShouldReturnListOfUsers() {
        // Given
        User secondUser = new User();
        secondUser.setId(2L);
        secondUser.setName("Vali");
        secondUser.setEmail("vali@example.com");
        secondUser.setRole(Role.ADMIN);

        List<User> userList = Arrays.asList(mockUser, secondUser);
        when(userService.findAll()).thenReturn(userList);

        // When
        ResponseEntity<List<User>> response = userController.findAll();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(Role.USER, response.getBody().get(0).getRole());
        assertEquals(Role.ADMIN, response.getBody().get(1).getRole());

        verify(userService, times(1)).findAll();
    }

    // --- GET /api/users/{id} (Uğurlu hal) ---
    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Given
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        // When
        ResponseEntity<User> response = userController.getUserById(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Ali", response.getBody().getName());
        assertEquals(Role.USER, response.getBody().getRole()); // Xətalı .get() metodu düzəldildi

        verify(userService, times(1)).getUserById(1L);
    }

    // --- GET /api/users/{id} (İstifadəçi tapılmadıqda) ---
    @Test
    void getUserById_WhenUserDoesNotExist_ShouldReturnNotFound() {
        // Given
        when(userService.getUserById(99L)).thenReturn(Optional.empty());

        // When
        ResponseEntity<User> response = userController.getUserById(99L);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(userService, times(1)).getUserById(99L);
    }

    // --- POST /api/users (Uğurlu hal) ---
    @Test
    void save_ShouldReturnCreatedStatus() {
        // Given
        doNothing().when(userService).save(any(User.class));

        // When
        ResponseEntity<String> response = userController.save(mockUser);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User added successfully.", response.getBody());

        verify(userService, times(1)).save(mockUser);
    }

    // --- POST /api/users (Xəta halı) ---
    @Test
    void save_WhenEmailExists_ShouldReturnBadRequest() {
        // Given
        doThrow(new RuntimeException("The account with this email exists."))
                .when(userService).save(any(User.class));

        // When
        ResponseEntity<String> response = userController.save(mockUser);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The account with this email exists.", response.getBody());
    }

    // --- PUT /api/users (Uğurlu hal) ---
    @Test
    void updateUser_ShouldReturnOkStatus() {
        // Given
        doNothing().when(userService).updateUser(any(User.class));

        // When
        ResponseEntity<String> response = userController.updateUser(mockUser);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User updated successfully.", response.getBody());

        verify(userService, times(1)).updateUser(mockUser);
    }

    // --- PUT /api/users (Xəta halı) ---
    @Test
    void updateUser_WhenUserDoesNotExist_ShouldReturnBadRequest() {
        // Given
        doThrow(new RuntimeException("User you want to update does not exist"))
                .when(userService).updateUser(any(User.class));

        // When
        ResponseEntity<String> response = userController.updateUser(mockUser);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User you want to update does not exist", response.getBody());
    }

    // --- DELETE /api/users/{id} (Uğurlu hal) ---
    @Test
    void deleteById_ShouldReturnOkStatus() {
        // Given
        doNothing().when(userService).deleteById(1L);

        // When
        ResponseEntity<String> response = userController.deleteById(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully.", response.getBody());

        verify(userService, times(1)).deleteById(1L);
    }

    // --- DELETE /api/users/{id} (Xəta halı) ---
    @Test
    void deleteById_WhenUserDoesNotExist_ShouldReturnNotFound() {
        // Given
        doThrow(new RuntimeException("User you want to delete does not exist"))
                .when(userService).deleteById(99L);

        // When
        ResponseEntity<String> response = userController.deleteById(99L);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User you want to delete does not exist", response.getBody());
    }
}