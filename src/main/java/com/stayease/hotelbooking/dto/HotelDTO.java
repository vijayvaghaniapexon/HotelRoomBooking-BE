package com.stayease.hotelbooking.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class HotelDTO {
    private UUID id;
    private String name;
    private String city;
    private Integer starRating;
    private String description;
    private String coverImageUrl;
    private UUID managerId;
}
