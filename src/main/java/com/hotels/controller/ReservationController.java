package com.hotels.controller;

import com.hotels.entities.reservation.ReservationDTO;
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
    public List<ReservationDTO> getAll() throws EntityNotFoundException {
        return this.reservationService.getAll();
    }

    //    localhost:8080/api/v1/person/get1/5
    @GetMapping("/{reservationNumber}")
    public ReservationDTO getReservationById(@PathVariable Integer reservationNumber) throws EntityNotFoundException {
        return reservationService.getReservationByReservationNum(reservationNumber);
    }

    @PostMapping("/insert/reservation")
    public void insertReservation(@RequestBody ReservationDTO reservationDTO) throws EntityNotFoundException {
        reservationService.insertReservation(reservationDTO);
    }

    @PostMapping("/insert/reservation_list")
    public void insertReservations(@RequestBody List<ReservationDTO> reservationsDTOs) throws EntityNotFoundException {
        reservationService.insertReservations(reservationsDTOs);
    }

    @PutMapping
    public void updateReservation(@RequestBody ReservationDTO reservationDTO) throws EntityNotFoundException {
        reservationService.updateReservation(reservationDTO);
    }

    @DeleteMapping
    public void deleteReservation(int resNum) throws EntityNotFoundException {
        reservationService.deleteReservation(resNum);
    }
}
