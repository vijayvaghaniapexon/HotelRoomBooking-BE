package com.stayease.hotelbooking.controller;
 
import com.stayease.hotelbooking.dto.UserOptionDTO;
import com.stayease.hotelbooking.entity.User;
import com.stayease.hotelbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
 
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
 
    private final UserRepository userRepository;

    @GetMapping("/assignable-managers")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserOptionDTO> getAssignableManagers() {
        return userRepository
                .findByIsVerifiedTrueAndRoleInOrderByNameAsc(List.of("MANAGER"))
                .stream()
                .map(user -> new UserOptionDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole()))
                .collect(Collectors.toList());
    }
 
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