package com.hotels.controller;

import com.hotels.entities.room.RoomDTO;
import com.hotels.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping(path = "/rooms")
public class RoomController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public List<RoomDTO> getAll() throws EntityNotFoundException {
        return this.roomService.getAll();
    }

    //    localhost:8080/api/v1/person/get1/5
    @GetMapping("/{roomId}")
    public RoomDTO getRoomById(@PathVariable Integer roomId) throws EntityNotFoundException {
        return roomService.getRoomById(roomId);
    }

    /*//http://localhost:8080/api/v1/person/get2?id=5
    @GetMapping
    public Room getPersonById2(int id) throws EntityNotFoundException{
        return roomService.getPersonById(id);
    }*/

    @PostMapping("/insert/room")
    public void insertRoom(@RequestBody RoomDTO roomDTO) throws EntityNotFoundException {
        roomService.insertRoom(roomDTO);
    }


    @PostMapping("/insert/room_list")
    public void insertReservations(@RequestBody List<RoomDTO> roomsDTOs) throws EntityNotFoundException {
        this.roomService.insertRooms(roomsDTOs);
    }

    @PutMapping
    public void updateRoom(@RequestBody RoomDTO roomDTO) throws EntityNotFoundException {
        roomService.updateRoom(roomDTO);
    }

    @DeleteMapping
    public void deleteRoom(int id) throws Exception {
        roomService.deleteRoom(id);
    }
}
