package com.stayease.hotelbooking.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class HotelResponse {

    private UUID id;
    private String name;
    private String city;
    private Integer starRating;
    private String description;
    private String coverImageUrl;
    private UUID managerId;
    private LocalDateTime createdAt;
}
