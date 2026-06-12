package com.stayease.hotelbooking.controller;

import com.stayease.hotelbooking.dto.HotelRequest;
import com.stayease.hotelbooking.dto.HotelResponse;
import com.stayease.hotelbooking.entity.Hotel;
import com.stayease.hotelbooking.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping
    public List<HotelResponse> getAllHotels() {
        return hotelService.getAllHotels().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public HotelResponse getHotelById(@PathVariable UUID id) {
        try {
            return toResponse(hotelService.getHotelById(id));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping
    public ResponseEntity<HotelResponse> createHotel(@RequestBody HotelRequest request) {
        Hotel hotel = hotelService.createHotel(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(hotel));
    }

    @PutMapping("/{id}")
    public HotelResponse updateHotel(@PathVariable UUID id,
                                     @RequestBody HotelRequest request) {
        try {
            return toResponse(hotelService.updateHotel(id, request));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable UUID id) {
        try {
            hotelService.deleteHotel(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    private HotelResponse toResponse(Hotel hotel) {
        HotelResponse response = new HotelResponse();
        response.setId(hotel.getId());
        response.setName(hotel.getName());
        response.setCity(hotel.getCity());
        response.setStarRating(hotel.getStarRating());
        response.setDescription(hotel.getDescription());
        response.setCoverImageUrl(hotel.getCoverImageUrl());
        response.setManagerId(hotel.getManagerId());
        response.setCreatedAt(hotel.getCreatedAt());
        return response;
    }
}
