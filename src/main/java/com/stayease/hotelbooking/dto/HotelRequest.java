package com.stayease.hotelbooking.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class HotelRequest {

    private String name;
    private String city;
    private Integer starRating;
    private String description;
    private String coverImageUrl;
    private UUID managerId;
}
