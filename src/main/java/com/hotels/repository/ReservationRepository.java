package com.hotels.repository;

import com.hotels.entities.roomreservationfeature.Reservation;
import com.hotels.entities.roomreservationfeature.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    // TODO: Not sure if this is the right way to do this.. very slow?
    List<Reservation> findReservationsByHotelId(Integer hotelId);
}
