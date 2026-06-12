package com.stayease.hotelbooking.controller;
 
import com.stayease.hotelbooking.entity.User;
import com.stayease.hotelbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
 
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
 
    private final UserRepository userRepository;
 
   @GetMapping("/me")
public Map<String, String> getCurrentUser(Authentication authentication) {
 
    String email = authentication.getName();
 
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
 
    Map<String, String> response = new HashMap<>();
 
    response.put("email", user.getEmail());
    response.put("name", user.getName());
    response.put("role", user.getRole());
 
    return response;
}
}