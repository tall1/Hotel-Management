package com.hotels.service;
import com.hotels.entities.roomreservationfeature.Room;

import java.sql.SQLException;
import java.util.List;

public interface RoomService {
     List<Room> getAll() throws SQLException;
     Room getRoomById(int id) throws SQLException;
     void insertRoom(Room room) throws SQLException;
     void updateRoom(Room room) throws SQLException;
     void deleteRoom(int id) throws SQLException;
}
