package com.hotels.controller;

import com.hotels.entities.reservation.ReservationDTO;
import com.hotels.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping(path = "/reservations")
@CrossOrigin(origins = "http://localhost:3000")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/")
    public List<ReservationDTO> getAll() {
        return this.reservationService.getAll();
    }

    @GetMapping("/get_reservations_by_user_id/{userId}")
    public List<ReservationDTO> getAllByUserId(@PathVariable int userId) throws EntityNotFoundException {
        return this.reservationService.getAllByUserId(userId);
    }

    //    localhost:8080/api/v1/person/get1/5
    @GetMapping("/{reservationNumber}")
    public ReservationDTO getReservationById(@PathVariable Integer reservationNumber) throws EntityNotFoundException {
        return reservationService.getReservationByReservationNum(reservationNumber);
    }

    @PostMapping("/insert/reservation")
    public int insertReservation(@RequestBody ReservationDTO reservationDTO) throws EntityNotFoundException {
        return reservationService.insertReservation(reservationDTO);
    }

    @PostMapping("/insert/reservation_list")
    public List<Integer> insertReservations(@RequestBody List<ReservationDTO> reservationsDTOs) throws EntityNotFoundException {
        return reservationService.insertReservations(reservationsDTOs);
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
