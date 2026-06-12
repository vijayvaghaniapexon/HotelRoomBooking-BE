package com.stayease.hotelbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class Hotel {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(name = "star_rating", nullable = false)
    private Integer starRating;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(name = "manager_id")
    private UUID managerId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
