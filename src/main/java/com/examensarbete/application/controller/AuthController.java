package com.examensarbete.application.controller;

import com.examensarbete.application.model.User;
import com.examensarbete.application.service.AuthService;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        boolean success = authService.register(user);
        if (success) {
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already taken");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user, HttpSession session) {
        if (authService.validateUser(user.getUsername(), user.getPassword())) {
            session.setAttribute("username", user.getUsername());
            session.setAttribute("user", authService.findUserByUsername(user.getUsername()));
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok().build();
    }

}


