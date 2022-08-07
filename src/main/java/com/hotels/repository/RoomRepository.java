package com.hotels.repository;

import com.hotels.entities.roomreservationfeature.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findRoomsByHotelId(Integer hotelId);
}
