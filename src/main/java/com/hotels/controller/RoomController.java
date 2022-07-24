package com.hotels.controller;

import com.hotels.entities.roomreservationfeature.Room;
import com.hotels.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(path = "/rooms")
public class RoomController {
    private RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public List<Room> getAll() throws SQLException {
        return this.roomService.getAll();
    }

//    localhost:8080/api/v1/person/get1/5
    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable int id) throws SQLException {
        return roomService.getRoomById(id);
    }

    /*//http://localhost:8080/api/v1/person/get2?id=5
    @GetMapping
    public Room getPersonById2(int id) throws SQLException {
        return roomService.getPersonById(id);
    }*/

    @PostMapping
    public void insertRoom(Room room) throws SQLException {
        roomService.insertRoom(room);
    }

    @PutMapping
    public void updateRoom(Room room) throws SQLException {
        roomService.updateRoom(room);
    }

    @DeleteMapping
    public void deleteRoom(int id) throws SQLException {
        roomService.deleteRoom(id);
    }
}
