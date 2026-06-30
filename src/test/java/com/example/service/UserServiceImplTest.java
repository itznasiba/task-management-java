package com.example.service;

import com.example.model.Role;
import com.example.model.User;
import com.example.repository.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepositoryImpl userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepositoryImpl.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    @DisplayName("Uğurlu Saxlama: Email unikal olduqda istifadəçi sistemə yazılmalıdır")
    void should_SaveUser_When_EmailIsUnique() {
        User newUser = new User(1L, "Kamil", "kamil@mail.com", Role.USER);
        when(userRepository.findAll()).thenReturn(Arrays.asList(
                new User(2L, "Aysel", "aysel@mail.com", Role.MANAGER)
        ));

        assertDoesNotThrow(() -> userService.save(newUser));
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    @DisplayName("Xəta: Eyni email ilə ikinci istifadəçi qeydiyyatdan keçə bilməz")
    void should_ThrowException_When_SavingUserWithDuplicateEmail() {
        User existingUser = new User(1L, "Kamil", "kamil@mail.com", Role.USER);
        User duplicateUser = new User(2L, "Murad", "KAMIL@mail.com", Role.ADMIN);
        when(userRepository.findAll()).thenReturn(Arrays.asList(existingUser));

        // DÜZƏLİŞ: Servisdəki "The account with this email exists." mesajı ilə uyğunlaşdırıldı
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.save(duplicateUser));
        assertEquals("The account with this email exists.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Uğurlu Yeniləmə: İstifadəçi mövcud olduqda updateUser işləməlidir")
    void should_UpdateUser_When_UserExists() {
        User existingUser = new User(1L, "Kamil", "kamil@mail.com", Role.USER);
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(existingUser));

        assertDoesNotThrow(() -> userService.updateUser(existingUser));

        // DÜZƏLİŞ: Çağırılan real metod updateStatus yox, updateUser-dir
        verify(userRepository, times(1)).updateUser(existingUser);
    }

    @Test
    @DisplayName("Xəta: Mövcud olmayan istifadəçi yenilənməyə çalışdıqda xəta atmalıdır")
    void should_ThrowException_When_UpdatingNonExistingUser() {
        User nonExistingUser = new User(99L, "Rauf", "rauf@mail.com", Role.USER);
        when(userRepository.getUserById(99L)).thenReturn(Optional.empty());

        // DÜZƏLİŞ: Servisdəki "User you want to update does not exist" mesajı ilə uyğunlaşdırıldı
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.updateUser(nonExistingUser));
        assertEquals("User you want to update does not exist", exception.getMessage());
        verify(userRepository, never()).updateUser(any(User.class));
    }

    @Test
    @DisplayName("Xəta: Silinmək istənən istifadəçi tapılmadıqda deleteById xəta atmalıdır")
    void should_ThrowException_When_DeletingNonExistingUser() {
        long userId = 55L;
        when(userRepository.getUserById(userId)).thenReturn(Optional.empty());

        // DÜZƏLİŞ: Servisdəki "User you want to delete does not exist" mesajı ilə uyğunlaşdırıldı
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.deleteById(userId));
        assertEquals("User you want to delete does not exist", exception.getMessage());
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    @DisplayName("Uğurlu Tapılma: getUserById ilə axtarış düzgün nəticə verməlidir")
    void should_ReturnUser_When_SearchingById() {
        long userId = 1L;
        User user = new User(userId, "Kamil", "kamil@mail.com", Role.USER);
        when(userRepository.getUserById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(userId);

        assertTrue(result.isPresent());
        assertEquals("Kamil", result.get().getName());
        assertEquals(Role.USER, result.get().getRole());
    }
}