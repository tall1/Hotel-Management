package com.hotels.repository;

import com.hotels.entities.roomreservationfeature.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    @Query(value = "select * from room r where r.hotel_id = :hotel_id and r.available_date <= :date", nativeQuery = true)
    List<Room> findRoomsByHotelIdAndAvailableDate(@Param("hotel_id") Integer hotelId, @Param("date") LocalDate date);

    @Query(value = "select r.id from room r where r.hotel_id = :hotel_id and r.room_number = :room_num", nativeQuery = true)
    Optional<Integer> findRoomByRoomNumberAndHotelId(@Param("hotel_id") Integer hotelId, @Param("room_num") Integer roomNumber);

    @Transactional
    @Modifying
    @Query(value = "UPDATE `hoteldb`.`room` SET `available_date` = :new_date WHERE (`id` = :room_id);", nativeQuery = true)
    void updateRoomAvailableDate(@Param("room_id") Integer roomId, @Param("new_date") LocalDate newDate);

    @Transactional
    @Modifying
    @Query(value = "delete from room r where r.id = :room_id", nativeQuery = true)
    void deleteRoomById(@Param("room_id") Integer roomId);
}
