package com.stayease.hotelbooking.repository;

import com.stayease.hotelbooking.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface HotelRepository extends JpaRepository<Hotel, UUID> {
    List<Hotel> findByCity(String city);
    List<Hotel> findByManagerId(UUID managerId);
}
