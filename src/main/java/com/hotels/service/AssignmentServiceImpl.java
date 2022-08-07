package com.hotels.service;

import com.hotels.Main;
import com.hotels.assignment.Assignment;
import com.hotels.assignment.SimpleAssignment;
import com.hotels.entities.userhotel.User;
import com.hotels.repository.EngineRepository;
import com.hotels.repository.ReservationRepository;
import com.hotels.repository.RoomRepository;
import com.hotels.repository.UserRepository;
import com.hotels.service.utils.EngineDTO;
import com.hotels.service.utils.EngineProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.SQLException;
import java.util.Optional;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    private final EngineRepository engineRep;
    private final RoomRepository roomRep;
    private final ReservationRepository resRep;
    private final UserRepository userRep;

    @Autowired
    public AssignmentServiceImpl(
            EngineRepository engineRep,
            RoomRepository roomRep,
            ReservationRepository resRep,
            UserRepository userRep) {
        this.engineRep = engineRep;
        this.roomRep = roomRep;
        this.resRep = resRep;
        this.userRep = userRep;
    }


    @PostConstruct
    public void postConstructor() {
        // here put any after construction operations
        System.out.println("AssignmentServiceImpl: @PostConstruct");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("AssignmentServiceImpl: @PreDestroy");
    }

    @Override
    public SimpleAssignment getAssignment(Integer userId) throws SQLException {
        // Retrieve User and EngineDTO:
        Optional<EngineDTO> eDto = this.engineRep.findById(userId);
        Optional<User> user = this.userRep.findById(userId);
        // Check present:
        if (eDto.isPresent() && user.isPresent()) {
            // Retrieve EngineProps from EngineDTO
            EngineProperties engineProps = new EngineProperties(eDto.get());
            // Retrieve hotelID from User:
            Integer hotelId = user.get().getHotel().getId();
            Assignment resAsmt =
                    Main.getAssignment(
                            engineProps,
                            roomRep.findRoomsByHotelId(hotelId),
                            resRep.findReservationsByHotelId(hotelId));
            return new SimpleAssignment(resAsmt);
        } else {
            throw new SQLException("EngineDTO not found");
        }
    }

    @Override
    public void insertEngineData(EngineDTO engineDTO) {
        this.engineRep.save(engineDTO);
    }

    @Override
    public void updateEngineData(EngineDTO engineDTO) {
        this.engineRep.save(engineDTO);
    }

    @Override
    public void deleteEngineData(Integer userId) {
        this.engineRep.deleteById(userId);
    }
}
