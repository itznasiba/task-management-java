package com.example.repository;

import com.example.AppConfig; // Sənin Spring konfiqurasiya klasın (DataSource və buralar təyin olunan yer)
import com.example.model.Role;
import com.example.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class) // Spring-i JUnit 5-ə inteqrasiya edirik (Boot-suz yol)
@ContextConfiguration(classes = {AppConfig.class, UserRepositoryImpl.class}) // Sənin Spring Bean-lərini yükləyirik
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Uğurlu Saxlama: Pure Spring mühitində istifadəçi bazaya yazılmalıdır")
    void testDatabaseOperations() {
        System.out.println("=== PURE SPRING REPOZİTORİYA TESTİ BAŞLADI ===");

        // Hər dəfə fərqli email generasiya edirik
        String uniqueEmail = "spring_" + System.currentTimeMillis() + "@mail.com";

        User user = new User();
        user.setName("Nasiba");
        user.setEmail(uniqueEmail);
        user.setRole(Role.USER);

        // WHEN
        userRepository.save(user);

        // THEN
        List<User> allUsers = userRepository.findAll();
        assertFalse(allUsers.isEmpty());

        Optional<User> savedUser = allUsers.stream()
                .filter(u -> u.getEmail().equals(uniqueEmail))
                .findFirst();

        assertTrue(savedUser.isPresent());
        assertEquals("Nasiba", savedUser.get().getName());
    }
}