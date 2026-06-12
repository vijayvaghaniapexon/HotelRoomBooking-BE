package com.stayease.hotelbooking.service;
 
import com.stayease.hotelbooking.entity.User;
import com.stayease.hotelbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
 
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
 
@Service
public class AuthService {
 
    @Autowired
    private UserRepository userRepository;
 
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();
 
    public String register(String name,
                           String email,
                           String password) {
        // Name validation
    if (name == null || name.trim().isEmpty()) {
        return "Name is required";
    }
 
    // Email validation
    if (email == null ||
            !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
        return "Invalid email format";
    }
 
    // Duplicate email validation
    if (userRepository.findByEmail(email).isPresent()) {
        return "Email already registered";
    }
 
    // Password validation
    if (!isValidPassword(password)) {
        return "Password must contain uppercase, lowercase, number, special character and minimum 8 characters";
    }
 
    // Existing logic starts here
       String otp = String.format("%06d",
                new Random().nextInt(999999));
 
        User user = new User();
 
        user.setId(UUID.randomUUID());
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
 
        user.setRole("User");
 
        user.setCreatedAt(LocalDateTime.now());
 
        user.setIsVerified(false);
 
        user.setOtp(otp);
 
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
 
        userRepository.save(user);
 
        return otp;
    }

    public String verifyOtp(String email, String otp) {
 
        Optional<User> optionalUser = userRepository.findByEmail(email);
 
    if (optionalUser.isEmpty()) {
        return "User not found";
    }
 
    User user = optionalUser.get();
 
    if (!otp.equals(user.getOtp())) {
        return "Invalid OTP";
    }
 
    if (LocalDateTime.now().isAfter(user.getOtpExpiry())) {
        return "OTP expired";
    }
 
    user.setIsVerified(true);
    user.setOtp(null);
    user.setOtpExpiry(null);
 
    userRepository.save(user);
 
    return "OTP verified successfully";
}

public Map<String, String> login(String email, String password) {
 
    Optional<User> optionalUser =
            userRepository.findByEmail(email);
 
    if (optionalUser.isEmpty()) {
        Map<String, String> response = new HashMap<>();
response.put("message", "Invalid email or password");
return response;
    }
 
    User user = optionalUser.get();
 
    if (!user.getIsVerified()) {
        Map<String, String> response = new HashMap<>();
response.put("message", "Please verify OTP first");
return response;
    }
 
    if (!passwordEncoder.matches(
            password,
            user.getPasswordHash())) {
 
       Map<String, String> response = new HashMap<>();
response.put("message", "Invalid email or password");
return response;
    }
 
   String token = jwtService.generateToken(email);
 
Map<String, String> response = new HashMap<>();
response.put("message", "Login successful");
response.put("token", token);
 
return response;
}

public String forgotPassword(String email) {
 
    Optional<User> optionalUser =
            userRepository.findByEmail(email);
 
    if (optionalUser.isEmpty()) {
        return "User not found";
    }
 
    User user = optionalUser.get();
 
    if (!Boolean.TRUE.equals(user.getIsVerified())) {
        return "Please verify your account first";
    }
 
    String otp = String.format("%06d",
            new Random().nextInt(999999));
 
    user.setOtp(otp);
 
    user.setOtpExpiry(
            LocalDateTime.now().plusMinutes(5));
 
    userRepository.save(user);
 
    return otp;
}

private boolean isValidPassword(String password) {
 
    return password != null &&
            password.matches(
                    "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
}

public String resetPassword(String email,
                            String otp,
                            String newPassword) {
 
    Optional<User> optionalUser =
            userRepository.findByEmail(email);
 
    if (optionalUser.isEmpty()) {
        return "User not found";
    }
 
    User user = optionalUser.get();
 
    if (user.getOtp() == null) {
        return "No OTP generated";
    }
 
    if (!otp.equals(user.getOtp())) {
        return "Invalid OTP";
    }
 
    if (LocalDateTime.now().isAfter(user.getOtpExpiry())) {
        return "OTP expired";
    }
 
    if (!isValidPassword(newPassword)) {
        return "Password must contain at least 8 characters, one uppercase letter, one lowercase letter, one digit and one special character";
    }
 
    user.setPasswordHash(
            passwordEncoder.encode(newPassword));
 
    user.setOtp(null);
 
    user.setOtpExpiry(null);
 
    userRepository.save(user);
 
    return "Password reset successful";
}

public String resendOtp(String email) {
 
    Optional<User> optionalUser =
            userRepository.findByEmail(email);
 
    if (optionalUser.isEmpty()) {
        return "User not found";
    }
 
    User user = optionalUser.get();
 
    if (Boolean.TRUE.equals(user.getIsVerified())) {
        return "User already verified";
    }
 
    String otp = String.format(
            "%06d",
            new Random().nextInt(999999));
 
    user.setOtp(otp);
 
    user.setOtpExpiry(
            LocalDateTime.now().plusMinutes(5));
 
    userRepository.save(user);
 
    return otp;
}

@Autowired
JwtService jwtService;

 
 
}