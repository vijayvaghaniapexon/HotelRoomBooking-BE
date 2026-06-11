package com.stayease.hotelbooking.entity;
 
import jakarta.persistence.*;
import lombok.Data;
 
import java.time.LocalDateTime;
import java.util.UUID;
 
@Entity
@Table(name = "users")
@Data
public class User {
 
    @Id
    private UUID id;
 
    @Column(nullable = false, unique = true)
    private String email;
 
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
 
    private String role;
 
    private String name;
 
    @Column(name = "created_at")
    private LocalDateTime createdAt;
 
    @Column(name = "is_verified")
    private Boolean isVerified;
 
    private String otp;
 
    @Column(name = "otp_expiry")
    private LocalDateTime otpExpiry;
}