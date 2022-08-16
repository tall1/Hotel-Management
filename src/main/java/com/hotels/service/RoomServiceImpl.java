package com.hotels.service;

import com.hotels.entities.room.Room;
import com.hotels.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @PostConstruct
    public void init453534() {
        // here put any after construction operations
        System.out.println("RoomServiceImpl: @PostConstruct");
    }

    @PreDestroy
    public void exitAll123() {
        System.out.println("RoomServiceImpl: @PreDestroy");
    }

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    @Override
    public Room getRoomById(int id) {
        if (roomRepository.findById(id).isPresent()) {
            return roomRepository.findById(id).get();
        }
        throw new EntityNotFoundException("Room " + id);
    }

    @Override
    public void insertRoom(Room room) {
        roomRepository.save(room);
    }

    @Override
    public void updateRoom(Room room) {
        roomRepository.save(room);
    }

    @Override
    public void deleteRoom(int id) {
        roomRepository.deleteRoomById(id);
    }
}
