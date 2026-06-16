package com.stayease.hotelbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserOptionDTO {
    private UUID id;
    private String name;
    private String email;
    private String role;
}
