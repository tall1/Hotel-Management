package com.hotels.service;

import com.hotels.Main;
import com.hotels.assignment.Assignment;
import com.hotels.assignment.SimpleAssignment;
import com.hotels.entities.roomreservationfeature.Reservation;
import com.hotels.entities.roomreservationfeature.Room;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    private final EngineRepository engineRep;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRep;
    private final UserRepository userRep;

    @Autowired
    public AssignmentServiceImpl(
            EngineRepository engineRep,
            RoomRepository roomRepository,
            ReservationRepository reservationRep,
            UserRepository userRep) {
        this.engineRep = engineRep;
        this.roomRepository = roomRepository;
        this.reservationRep = reservationRep;
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
    public SimpleAssignment computeAssignmentByDate(Integer userId, LocalDate date) throws SQLException {
        Integer hotelId = findHotelByUserId(userId);
        EngineProperties engineProps = getEnginePropertiesByUserId(userId);

        List<Room> roomList = roomRepository.findRoomsByHotelIdAndAvailableDate(hotelId, date);
        List<Reservation> reservationList = reservationRep.findReservationsByHotelIdAndCheckinDate(hotelId, date);

        Assignment resultAssignment =
                Main.getAssignment(
                        engineProps,
                        roomList,
                        reservationList);
        return new SimpleAssignment(resultAssignment, hotelId);
    }

    private EngineProperties getEnginePropertiesByUserId(Integer userId) throws SQLException {
        Optional<EngineDTO> eDto = this.engineRep.findById(userId);
        if (!eDto.isPresent()) {
            throw new SQLException("Engine properties not found!");
        }
        // Retrieve EngineProps from EngineDTO:
        return new EngineProperties(eDto.get());
    }

    private int findHotelByUserId(Integer userId) throws SQLException {
        Optional<Integer> hotelIdOpt = this.userRep.findHotelIdByUserId(userId);
        if (!hotelIdOpt.isPresent()) {
            throw new SQLException("User not found!");
        }
        return hotelIdOpt.get();
    }

    @Override
    public void updateRoomsAvailableDate(SimpleAssignment chosenAssignment) throws SQLException {
        // Update rooms available date according to chosen assignment:
        Map<Integer, Integer> reservationRoomMap = chosenAssignment.getReservationRoomHashMap();
        int hotelId = chosenAssignment.getHotelId();
        for (Map.Entry<Integer, Integer> entry : reservationRoomMap.entrySet()) {
            LocalDate reservationCheckoutDate = getReservationCheckoutDateByResNum(entry.getKey());
            Integer roomId = getRoomIdByHotelIdAndRoomNumber(hotelId, entry.getValue());
            this.roomRepository.updateRoomAvailableDate(roomId, reservationCheckoutDate);
        }
    }

    private LocalDate getReservationCheckoutDateByResNum(int reservationNum) throws SQLException {
        Optional<LocalDate> checkoutOpt = reservationRep.findCheckoutDateByReservationNumber(reservationNum);
        if (!checkoutOpt.isPresent()) {
            throw new SQLException("Reservation" + reservationNum + " not found!");
        }
        return checkoutOpt.get();
    }

    private Integer getRoomIdByHotelIdAndRoomNumber(int hotelId, int roomNum) throws SQLException {
        Optional<Integer> roomIdOpt = roomRepository.findRoomByRoomNumberAndHotelId(hotelId, roomNum);
        if (!roomIdOpt.isPresent()) {
            throw new SQLException("Room " + roomNum + " in hotel " + hotelId + "not found!");
        }
        return roomIdOpt.get();
    }
}
