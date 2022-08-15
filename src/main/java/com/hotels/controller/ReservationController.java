package com.hotels.controller;

import com.hotels.entities.roomreservationfeature.Reservation;
import com.hotels.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping(path = "/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/")
    public List<Reservation> getAll() throws EntityNotFoundException {
        return this.reservationService.getAll();
    }

    //    localhost:8080/api/v1/person/get1/5
    @GetMapping("/{reservationNumber}")
    public Reservation getReservationById(@PathVariable Integer reservationNumber) throws EntityNotFoundException {
        return reservationService.getReservationById(reservationNumber);
    }

    /*//http://localhost:8080/api/v1/person/get2?id=5
    @GetMapping
    public Reservation getPersonById2(int id) throws EntityNotFoundException{
        return reservationService.getPersonById(id);
    }*/

    @PostMapping
    public void insertReservation(Reservation reservation) throws EntityNotFoundException {
        reservationService.insertReservation(reservation);
    }

    @PutMapping
    public void updateReservation(Reservation reservation) throws EntityNotFoundException {
        reservationService.updateReservation(reservation);
    }

    @DeleteMapping
    public void deleteReservation(Integer resNum) throws EntityNotFoundException {
        reservationService.deleteReservation(resNum);
    }
}
