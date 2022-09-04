package com.hotels.service;

import com.hotels.entities.reservation.ReservationDTO;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface ReservationService {
    List<ReservationDTO> getAll();

    List<ReservationDTO> getAllByUserId(int userId) throws EntityNotFoundException;

    ReservationDTO getReservationByReservationNum(int resNum) throws EntityNotFoundException;

    int insertReservation(ReservationDTO reservationDTO) throws EntityNotFoundException;

    List<Integer> insertReservations(List<ReservationDTO> reservationDTO) throws EntityNotFoundException;

    void updateReservation(ReservationDTO reservationDTO) throws EntityNotFoundException;

    void deleteReservation(Integer resNum) throws EntityNotFoundException;
}
