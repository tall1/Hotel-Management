package com.hotels.service;

import com.hotels.entities.roomreservationfeature.Reservation;

import java.sql.SQLException;
import java.util.List;

public interface ReservationService {
     List<Reservation> getAll() throws SQLException;
     Reservation getReservationById(int id) throws SQLException;
     void insertReservation(Reservation reservation) throws SQLException;
     void updateReservation(Reservation reservation) throws SQLException;
     void deleteReservation(int id) throws SQLException;
}
