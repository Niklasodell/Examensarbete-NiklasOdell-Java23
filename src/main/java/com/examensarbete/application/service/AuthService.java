package com.examensarbete.application.service;

import com.examensarbete.application.model.User;
import com.examensarbete.application.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public boolean register(User user) {
        System.out.println("Försöker spara användare: " + user.getUsername());
        if (userRepository.existsByUsernameIgnoreCase(user.getUsername())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean validateUser(String username, String rawPassword) {
        return userRepository.findByUsernameIgnoreCase(username)
                .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(false);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username).orElse(null);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

}
