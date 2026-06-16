package com.stayease.hotelbooking.service;

import com.stayease.hotelbooking.dto.HotelDTO;
import com.stayease.hotelbooking.entity.Hotel;
import com.stayease.hotelbooking.entity.User;
import com.stayease.hotelbooking.repository.HotelRepository;
import com.stayease.hotelbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;

    public List<HotelDTO> getAllHotels() {
        return hotelRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public HotelDTO getHotelById(UUID id) {
        Hotel hotel = hotelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Hotel not found"));
        return convertToDTO(hotel);
    }

    public List<HotelDTO> getHotelsByCity(String city) {
        return hotelRepository.findByCity(city).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public HotelDTO createHotel(HotelDTO hotelDTO) {
        validateManagerAssignment(hotelDTO.getManagerId());
        Hotel hotel = convertToEntity(hotelDTO);
        Hotel savedHotel = hotelRepository.save(hotel);
        return convertToDTO(savedHotel);
    }

    public HotelDTO updateHotel(UUID id, HotelDTO hotelDTO) {
        Hotel hotel = hotelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Hotel not found"));

        validateManagerAssignment(hotelDTO.getManagerId());
        
        hotel.setName(hotelDTO.getName());
        hotel.setCity(hotelDTO.getCity());
        hotel.setStarRating(hotelDTO.getStarRating());
        hotel.setDescription(hotelDTO.getDescription());
        hotel.setCoverImageUrl(hotelDTO.getCoverImageUrl());
        hotel.setManagerId(hotelDTO.getManagerId());
        
        Hotel updatedHotel = hotelRepository.save(hotel);
        return convertToDTO(updatedHotel);
    }

    public void deleteHotel(UUID id) {
        Hotel hotel = hotelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Hotel not found"));
        hotelRepository.delete(hotel);
    }

    private HotelDTO convertToDTO(Hotel hotel) {
        HotelDTO dto = new HotelDTO();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setCity(hotel.getCity());
        dto.setStarRating(hotel.getStarRating());
        dto.setDescription(hotel.getDescription());
        dto.setCoverImageUrl(hotel.getCoverImageUrl());
        dto.setManagerId(hotel.getManagerId());
        return dto;
    }

    private Hotel convertToEntity(HotelDTO dto) {
        Hotel hotel = new Hotel();
        hotel.setId(dto.getId() != null ? dto.getId() : UUID.randomUUID());
        hotel.setName(dto.getName());
        hotel.setCity(dto.getCity());
        hotel.setStarRating(dto.getStarRating());
        hotel.setDescription(dto.getDescription());
        hotel.setCoverImageUrl(dto.getCoverImageUrl());
        hotel.setManagerId(dto.getManagerId());
        return hotel;
    }

    private void validateManagerAssignment(UUID managerId) {
        if (managerId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Manager assignment is required");
        }

        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Selected manager does not exist"));

        if (!Boolean.TRUE.equals(manager.getIsVerified())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Manager must be verified");
        }

        if (manager.getRole() == null || !"MANAGER".equalsIgnoreCase(manager.getRole().trim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selected user is not a manager");
        }
    }
}
