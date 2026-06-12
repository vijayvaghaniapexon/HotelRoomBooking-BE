package com.stayease.hotelbooking.service;

import com.stayease.hotelbooking.dto.HotelRequest;
import com.stayease.hotelbooking.entity.Hotel;
import com.stayease.hotelbooking.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public Hotel getHotelById(UUID id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hotel not found"));
    }

    public Hotel createHotel(HotelRequest request) {
        Hotel hotel = new Hotel();
        hotel.setId(UUID.randomUUID());
        hotel.setName(request.getName());
        hotel.setCity(request.getCity());
        hotel.setStarRating(request.getStarRating());
        hotel.setDescription(request.getDescription());
        hotel.setCoverImageUrl(request.getCoverImageUrl());
        hotel.setManagerId(request.getManagerId());
        hotel.setCreatedAt(LocalDateTime.now());
        return hotelRepository.save(hotel);
    }

    public Hotel updateHotel(UUID id, HotelRequest request) {
        Hotel hotel = getHotelById(id);
        hotel.setName(request.getName());
        hotel.setCity(request.getCity());
        hotel.setStarRating(request.getStarRating());
        hotel.setDescription(request.getDescription());
        hotel.setCoverImageUrl(request.getCoverImageUrl());
        hotel.setManagerId(request.getManagerId());
        return hotelRepository.save(hotel);
    }

    public void deleteHotel(UUID id) {
        if (!hotelRepository.existsById(id)) {
            throw new IllegalArgumentException("Hotel not found");
        }
        hotelRepository.deleteById(id);
    }
}
