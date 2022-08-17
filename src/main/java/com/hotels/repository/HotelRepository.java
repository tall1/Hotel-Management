package com.hotels.repository;

import com.hotels.entities.hotel.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    @Modifying
    @Query(value = "delete from hotel h where h.id = :hotel_id", nativeQuery = true)
    void deleteHotelById(@Param("hotel_id") Integer hotelId);
}
