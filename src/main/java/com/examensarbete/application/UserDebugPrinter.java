package com.examensarbete.application;

import com.examensarbete.application.model.User;
import com.examensarbete.application.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDebugPrinter {

    private final UserRepository userRepository;

    public UserDebugPrinter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void printAllUsers() {
        List<User> users = userRepository.findAll();
        System.out.println("Alla användare i databasen:");
        if (users.isEmpty()) {
            System.out.println("Inga användare hittades.");
        } else {
            for (User user : users) {
                System.out.println(" - ID: " + user.getId() + ", Användarnamn: " + user.getUsername());
            }
        }
    }
}
