package com.hotels.service;

import com.hotels.entities.room.RoomDTO;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface RoomService {
     List<RoomDTO> getAll() throws EntityNotFoundException;
     RoomDTO getRoomById(int id) throws EntityNotFoundException;
     void insertRoom(RoomDTO roomDTO) throws EntityNotFoundException;
     void updateRoom(RoomDTO roomDTO) throws EntityNotFoundException;
     void deleteRoom(int id) throws EntityNotFoundException;
}

