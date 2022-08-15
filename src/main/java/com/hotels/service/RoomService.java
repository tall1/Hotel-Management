package com.hotels.service;

import com.hotels.entities.roomreservationfeature.Room;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface RoomService {
     List<Room> getAll() throws EntityNotFoundException;
     Room getRoomById(int id) throws EntityNotFoundException;
     void insertRoom(Room room) throws EntityNotFoundException;
     void updateRoom(Room room) throws EntityNotFoundException;
     void deleteRoom(int id) throws EntityNotFoundException;
}
