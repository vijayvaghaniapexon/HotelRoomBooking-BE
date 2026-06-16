-- Hotel Room Booking Database Schema
-- This script creates the required database and tables, then seeds initial data.
-- Based on application.properties: spring.datasource.url=jdbc:postgresql://localhost:5432/HotelRoomBooking

DO $$
BEGIN
   IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'HotelRoomBooking') THEN
      CREATE DATABASE "HotelRoomBooking";
   END IF;
END
$$;

-- When running manually in psql, connect to the database after creation:
-- \c "HotelRoomBooking"

CREATE TABLE IF NOT EXISTS users (
  id UUID PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(50),
  name VARCHAR(255),
  created_at TIMESTAMP,
  is_verified BOOLEAN,
  otp VARCHAR(20),
  otp_expiry TIMESTAMP
);

CREATE TABLE IF NOT EXISTS hotel (
  id UUID PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  city VARCHAR(255) NOT NULL,
  star_rating INTEGER,
  description TEXT,
  cover_image_url VARCHAR(1024),
  manager_id UUID,
  created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS room (
  id UUID PRIMARY KEY,
  hotel_id UUID NOT NULL,
  room_number VARCHAR(50) NOT NULL,
  room_type VARCHAR(100),
  price_per_night NUMERIC(12,2),
  max_occupancy INTEGER,
  description TEXT,
  image_url VARCHAR(1024),
  is_active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP,
  FOREIGN KEY (hotel_id) REFERENCES hotel(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS booking (
  id UUID PRIMARY KEY,
  user_id UUID NOT NULL,
  room_id UUID NOT NULL,
  status VARCHAR(50),
  check_in_date DATE,
  check_out_date DATE,
  total_price NUMERIC(12,2),
  created_at TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (room_id) REFERENCES room(id) ON DELETE CASCADE
);

-- Seed Data for Users (run after user table is created by Hibernate)
INSERT INTO users(id, email, password_hash, role, name, created_at, is_verified)
VALUES 
  ('550e8400-e29b-41d4-a716-446655440001', 'admin@stayease.com', '$2a$10$yTzINpOwGa5AjjlGF//uCO74tZI.rzUORrUtkDwkFn4y1BQXob5Qq', 'ADMIN', 'System Admin', NOW(), true),
  ('550e8400-e29b-41d4-a716-446655440002', 'manager1@stayease.com', '$2a$10$yTzINpOwGa5AjjlGF//uCO74tZI.rzUORrUtkDwkFn4y1BQXob5Qq', 'MANAGER', 'Hotel Manager 1', NOW(), true),
  ('550e8400-e29b-41d4-a716-446655440003', 'guest1@stayease.com', '$2a$10$yTzINpOwGa5AjjlGF//uCO74tZI.rzUORrUtkDwkFn4y1BQXob5Qq', 'GUEST', 'Guest User 1', NOW(), true);

-- Seed Data for Hotels
INSERT INTO hotel(id, name, city, star_rating, description, cover_image_url, manager_id, created_at)
VALUES 
  ('650e8400-e29b-41d4-a716-446655440001', 'Sunset Palms', 'Goa', 4, 'Beachfront resort with pool and water sports', 'https://images.unsplash.com/photo-1551632786-de41ec16a41b?auto=format&fit=crop&w=900&q=80', '550e8400-e29b-41d4-a716-446655440002', NOW()),
  ('650e8400-e29b-41d4-a716-446655440002', 'City Inn', 'Goa', 3, 'Budget-friendly city centre hotel', 'https://images.unsplash.com/photo-1568495248636-6432d2e5d0d1?auto=format&fit=crop&w=900&q=80', '550e8400-e29b-41d4-a716-446655440002', NOW()),
  ('650e8400-e29b-41d4-a716-446655440003', 'Azure Garden Hotel', 'San Francisco', 4, 'A calm downtown hotel with modern rooms and rooftop dining', 'https://images.unsplash.com/photo-1550355291-bbee04a92027?auto=format&fit=crop&w=900&q=80', '550e8400-e29b-41d4-a716-446655440002', NOW()),
  ('650e8400-e29b-41d4-a716-446655440004', 'Skyline Suite Resort', 'Chicago', 5, 'Luxury suites near the lake with premium service and views', 'https://images.unsplash.com/photo-1512453066654-03b87a7dfa3c?auto=format&fit=crop&w=900&q=80', '550e8400-e29b-41d4-a716-446655440002', NOW());

-- Seed Data for Rooms
INSERT INTO room(id, hotel_id, room_number, room_type, price_per_night, max_occupancy, description, image_url, is_active, created_at)
VALUES 
  -- Sunset Palms rooms
  ('750e8400-e29b-41d4-a716-446655440001', '650e8400-e29b-41d4-a716-446655440001', '101', 'Double', 3500.00, 2, 'Spacious double room with sea view', 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304?auto=format&fit=crop&w=900&q=80', true, NOW()),
  ('750e8400-e29b-41d4-a716-446655440002', '650e8400-e29b-41d4-a716-446655440001', '201', 'Suite', 7000.00, 3, 'Luxury suite with balcony and premium amenities', 'https://images.unsplash.com/photo-1648066267326-b24f7eb2b0f8?auto=format&fit=crop&w=900&q=80', true, NOW()),
  ('750e8400-e29b-41d4-a716-446655440003', '650e8400-e29b-41d4-a716-446655440001', '102', 'Single', 2000.00, 1, 'Cozy single room', 'https://images.unsplash.com/photo-1493857671505-72967e2e2760?auto=format&fit=crop&w=900&q=80', true, NOW()),
  
  -- City Inn rooms
  ('750e8400-e29b-41d4-a716-446655440004', '650e8400-e29b-41d4-a716-446655440002', '101', 'Single', 1500.00, 1, 'Budget single room in city center', 'https://images.unsplash.com/photo-1532729147b3ec8b8a0e75f5127d0e8e0c1e0e0c?auto=format&fit=crop&w=900&q=80', true, NOW()),
  ('750e8400-e29b-41d4-a716-446655440005', '650e8400-e29b-41d4-a716-446655440002', '102', 'Double', 2500.00, 2, 'Comfortable double room', 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&w=900&q=80', true, NOW()),
  
  -- Azure Garden Hotel rooms
  ('750e8400-e29b-41d4-a716-446655440006', '650e8400-e29b-41d4-a716-446655440003', '201', 'Double', 250.00, 2, 'Modern double room with city view', 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304?auto=format&fit=crop&w=900&q=80', true, NOW()),
  ('750e8400-e29b-41d4-a716-446655440007', '650e8400-e29b-41d4-a716-446655440003', '301', 'Suite', 450.00, 4, 'Premium suite with living area', 'https://images.unsplash.com/photo-1648066267326-b24f7eb2b0f8?auto=format&fit=crop&w=900&q=80', true, NOW()),
  
  -- Skyline Suite Resort rooms
  ('750e8400-e29b-41d4-a716-446655440008', '650e8400-e29b-41d4-a716-446655440004', '1001', 'Suite', 500.00, 3, 'Luxury suite with lake view', 'https://images.unsplash.com/photo-1648066267326-b24f7eb2b0f8?auto=format&fit=crop&w=900&q=80', true, NOW()),
  ('750e8400-e29b-41d4-a716-446655440009', '650e8400-e29b-41d4-a716-446655440004', '1002', 'Suite', 550.00, 4, 'Premium suite with jacuzzi', 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304?auto=format&fit=crop&w=900&q=80', true, NOW());

-- Notes on default passwords:
-- All test users have password: "Test@1234"
-- Bcrypt hash (with salt) for "Test@1234": $2a$10$yTzINpOwGa5AjjlGF//uCO74tZI.rzUORrUtkDwkFn4y1BQXob5Qq
-- 
-- Test Credentials:
-- Admin: admin@stayease.com / Test@1234
-- Manager: manager1@stayease.com / Test@1234
-- Guest: guest1@stayease.com / Test@1234

-- Optional: Create indexes for frequently queried fields
CREATE INDEX idx_hotel_city ON hotel(city);
CREATE INDEX idx_hotel_manager_id ON hotel(manager_id);
CREATE INDEX idx_room_hotel_id ON room(hotel_id);
CREATE INDEX idx_room_is_active ON room(is_active);
CREATE INDEX idx_booking_user_id ON booking(user_id);
CREATE INDEX idx_booking_room_id ON booking(room_id);
CREATE INDEX idx_booking_status ON booking(status);
CREATE INDEX idx_booking_check_dates ON booking(check_in_date, check_out_date);
