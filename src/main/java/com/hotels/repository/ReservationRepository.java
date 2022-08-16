package com.hotels.repository;

import com.hotels.entities.reservation.Reservation;
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
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query(value = "select * from reservation r where r.hotel_id = :hotel_id and r.checkin = :date", nativeQuery = true)
    List<Reservation> findReservationsByHotelIdAndCheckinDate(@Param("hotel_id") Integer hotelId, @Param("date") LocalDate date);

    @Query(value = "select * from reservation r where r.hotel_id = :hotel_id", nativeQuery = true)
    List<Reservation> findReservationsByHotelId(@Param("hotel_id") Integer hotelId);

    @Query(value = "select checkout from reservation r where r.reservation_number = :reservation_number", nativeQuery = true)
    Optional<LocalDate> findCheckoutDateByReservationNumber(@Param("reservation_number") Integer resNum);

    @Transactional
    @Modifying
    @Query(value = "delete from reservation r where r.reservation_number = :reservation_number", nativeQuery = true)
    void deleteReservationByReservationNumber(@Param("reservation_number") Integer resNum);
}
