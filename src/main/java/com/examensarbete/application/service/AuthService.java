package com.examensarbete.application.service;

import com.examensarbete.application.model.User;
import com.examensarbete.application.repository.UserRepository;
import com.examensarbete.application.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public String login(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername()).orElseThrow();
        if (passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return JwtUtil.generateToken(existingUser.getUsername());
        }
        throw new RuntimeException("Invalid credentials");
    }
}



