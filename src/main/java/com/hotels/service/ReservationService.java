package com.hotels.service;

import com.hotels.entities.roomreservationfeature.Reservation;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface ReservationService {
     List<Reservation> getAll() throws EntityNotFoundException;
     Reservation getReservationById(int id) throws EntityNotFoundException;
     void insertReservation(Reservation reservation) throws EntityNotFoundException;
     void updateReservation(Reservation reservation) throws EntityNotFoundException;
     void deleteReservation(int id) throws EntityNotFoundException;
}
