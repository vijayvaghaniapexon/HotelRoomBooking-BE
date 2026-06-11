package com.stayease.hotelbooking.controller;
 
import com.stayease.hotelbooking.dto.ForgotPasswordRequest;
import com.stayease.hotelbooking.dto.LoginRequest;
import com.stayease.hotelbooking.dto.RegisterRequest;
import com.stayease.hotelbooking.dto.ResetPasswordRequest;
import com.stayease.hotelbooking.dto.VerifyOtpRequest;
import com.stayease.hotelbooking.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
 
import java.util.HashMap;
import java.util.Map;
 
@RestController
@RequestMapping("/api/auth")
public class AuthController {
 
    @Autowired
    private AuthService authService;
 
    @PostMapping("/register")
    public Map<String, String> register(
            @RequestBody RegisterRequest request) {
 
        String otp = authService.register(
                request.getName(),
                request.getEmail(),
                request.getPassword());
 
        Map<String, String> response = new HashMap<>();
 
        if ("Email already registered".equals(otp)) {
            response.put("message", otp);
        } else {
            response.put("message", "OTP generated successfully");
            response.put("otp", otp);
        }
 
        return response;
    }

    @PostMapping("/verify-otp")
public Map<String, String> verifyOtp(
        @RequestBody VerifyOtpRequest request) {
 
    String result = authService.verifyOtp(
            request.getEmail(),
            request.getOtp());
 
    Map<String, String> response = new HashMap<>();
 
    response.put("message", result);
 
    return response;
}

@PostMapping("/login")
public Map<String, String> login(
        @RequestBody LoginRequest request) {
 
    String result =
            authService.login(
                    request.getEmail(),
                    request.getPassword());
 
    Map<String, String> response =
            new HashMap<>();
 
    response.put("message", result);
 
    return response;
}

@PostMapping("/forgot-password")
public Map<String, String> forgotPassword(
        @RequestBody ForgotPasswordRequest request) {
 
    String result =
            authService.forgotPassword(
                    request.getEmail());
 
    Map<String, String> response =
            new HashMap<>();
 
    if ("User not found".equals(result) ||
        "Please verify your account first".equals(result)) {
 
        response.put("message", result);
 
    } else {
 
        response.put("message",
                "OTP generated successfully");
 
        response.put("otp", result);
    }
 
    return response;
}

@PostMapping("/reset-password")
public Map<String, String> resetPassword(
        @RequestBody ResetPasswordRequest request) {
 
    String result =
            authService.resetPassword(
                    request.getEmail(),
                    request.getOtp(),
                    request.getNewPassword());
 
    Map<String, String> response =
            new HashMap<>();
 
    response.put("message", result);
 
    return response;
}
}
 