package com.examensarbete.application.service;

import com.examensarbete.application.model.User;
import com.examensarbete.application.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
        import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Successful() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("plaintext");

        when(userRepository.existsByUsernameIgnoreCase("testuser")).thenReturn(false);
        when(passwordEncoder.encode("plaintext")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        boolean result = authService.register(user);

        assertTrue(result);
        assertEquals("hashed", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void testRegister_UsernameAlreadyExists() {
        User user = new User();
        user.setUsername("existingUser");

        when(userRepository.existsByUsernameIgnoreCase("existingUser")).thenReturn(true);

        boolean result = authService.register(user);

        assertFalse(result);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testValidateUser_ValidCredentials() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("hashed");

        when(userRepository.findByUsernameIgnoreCase("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plaintext", "hashed")).thenReturn(true);

        boolean result = authService.validateUser("user", "plaintext");

        assertTrue(result);
    }

    @Test
    void testValidateUser_InvalidPassword() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("hashed");

        when(userRepository.findByUsernameIgnoreCase("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpass", "hashed")).thenReturn(false);

        boolean result = authService.validateUser("user", "wrongpass");

        assertFalse(result);
    }

    @Test
    void testValidateUser_UserNotFound() {
        when(userRepository.findByUsernameIgnoreCase("nouser")).thenReturn(Optional.empty());

        boolean result = authService.validateUser("nouser", "whatever");

        assertFalse(result);
    }

    @Test
    void testFindUserByUsername_UserExists() {
        User user = new User();
        user.setUsername("findme");

        when(userRepository.findByUsernameIgnoreCase("findme")).thenReturn(Optional.of(user));

        User found = authService.findUserByUsername("findme");

        assertNotNull(found);
        assertEquals("findme", found.getUsername());
    }

    @Test
    void testFindUserByUsername_UserNotFound() {
        when(userRepository.findByUsernameIgnoreCase("missing")).thenReturn(Optional.empty());

        User found = authService.findUserByUsername("missing");

        assertNull(found);
    }

    @Test
    void testDeleteUser_CallsRepository() {
        authService.deleteUser(42L);

        verify(userRepository).deleteById(42L);
    }
}
