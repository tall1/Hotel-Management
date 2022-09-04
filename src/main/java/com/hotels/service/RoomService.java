package com.hotels.service;

import com.hotels.entities.room.RoomDTO;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLException;
import java.util.List;

public interface RoomService {
     List<RoomDTO> getAll() throws EntityNotFoundException;
     List<RoomDTO> getAllByUserId(int userId) throws EntityNotFoundException;
     RoomDTO getRoomById(int id) throws EntityNotFoundException;
     int insertRoom(RoomDTO roomDTO) throws EntityNotFoundException;
     List<Integer> insertRooms(List<RoomDTO> roomsDTOs) throws EntityNotFoundException;
     void updateRoom(RoomDTO roomDTO) throws EntityNotFoundException;
     void deleteRoom(int id) throws EntityNotFoundException, SQLException;
}

