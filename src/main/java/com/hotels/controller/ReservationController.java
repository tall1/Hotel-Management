package com.hotels.controller;

import com.hotels.entities.roomreservationfeature.Reservation;
import com.hotels.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(path = "/reservations")
public class ReservationController {
    private ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/")
    public List<Reservation> getAll() throws SQLException {
        return this.reservationService.getAll();
    }

//    localhost:8080/api/v1/person/get1/5
    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable int id) throws SQLException {
        return reservationService.getReservationById(id);
    }

    /*//http://localhost:8080/api/v1/person/get2?id=5
    @GetMapping
    public Reservation getPersonById2(int id) throws SQLException {
        return reservationService.getPersonById(id);
    }*/

    @PostMapping
    public void insertReservation(Reservation reservation) throws SQLException {
        reservationService.insertReservation(reservation);
    }

    @PutMapping
    public void updateReservation(Reservation reservation) throws SQLException {
        reservationService.updateReservation(reservation);
    }

    @DeleteMapping
    public void deleteReservation(int id) throws SQLException {
        reservationService.deleteReservation(id);
    }
}
