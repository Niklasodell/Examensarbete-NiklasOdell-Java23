package com.examensarbete.application.service;

import com.examensarbete.application.model.User;
import com.examensarbete.application.repository.UserRepository;
import jakarta.transaction.*;
import org.springframework.beans.factory.annotation.Autowired;
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

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean validateUser(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(false);
    }
}





