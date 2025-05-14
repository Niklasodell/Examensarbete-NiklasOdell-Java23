package com.examensarbete.application.service;

import com.examensarbete.application.model.*;
import com.examensarbete.application.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public void register(User user) {
        userRepository.save(user);
    }

    public String login(User user) {
        User foundUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (foundUser.getPassword().equals(user.getPassword())) {
            return "JWT_TOKEN";
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}
