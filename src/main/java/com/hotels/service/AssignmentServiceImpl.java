package com.hotels.service;

import com.hotels.Main;
import com.hotels.entities.assignment.Assignment;
import com.hotels.entities.assignment.AssignmentDTO;
import com.hotels.entities.reservation.Reservation;
import com.hotels.entities.room.Room;
import com.hotels.repository.EngineRepository;
import com.hotels.repository.ReservationRepository;
import com.hotels.repository.RoomRepository;
import com.hotels.repository.UserRepository;
import com.hotels.entities.engine.Engine;
import com.hotels.entities.engine.EngineProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
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
    public AssignmentDTO computeAssignmentByDate(Integer userId, LocalDate date) throws EntityNotFoundException {
        Integer hotelId = findHotelByUserId(userId);
        EngineProperties engineProps = getEnginePropertiesByUserId(userId);

        List<Room> roomList = roomRepository.findRoomsByHotelIdAndAvailableDate(hotelId, date);
        List<Reservation> reservationList = reservationRep.findReservationsByHotelIdAndCheckinDate(hotelId, date);

        Assignment resultAssignment =
                Main.getAssignment(
                        engineProps,
                        roomList,
                        reservationList);
        AssignmentDTO assignmentDTO = new AssignmentDTO();
        assignmentDTO.setHotelId(hotelId);
        assignmentDTO.setReservationRoomMap(resultAssignment.getReservationRoomMap());
        return assignmentDTO;
    }

    private EngineProperties getEnginePropertiesByUserId(Integer userId) throws EntityNotFoundException {
        Optional<Engine> engine = this.engineRep.findById(userId);
        engine.orElseThrow(() -> new EntityNotFoundException("Engine properties for user "+ userId + " not found!"));
        return new EngineProperties(engine.get());
    }

    private int findHotelByUserId(Integer userId) throws EntityNotFoundException {
        Optional<Integer> hotelIdOpt = this.userRep.findHotelIdByUserId(userId);
        hotelIdOpt.orElseThrow(() -> new EntityNotFoundException("AssignmentServiceImpl: User with id " + userId + " not found!"));
        return hotelIdOpt.get();
    }

    @Override
    @Transactional
    public void updateRoomsAvailableDate(AssignmentDTO chosenAssignment) throws EntityNotFoundException {
        // Update rooms available date according to chosen assignment:
        Map<Integer, Integer> reservationRoomMap = chosenAssignment.getReservationRoomMap();
        int hotelId = chosenAssignment.getHotelId();
        for (Map.Entry<Integer, Integer> entry : reservationRoomMap.entrySet()) {
            LocalDate reservationCheckoutDate = getReservationCheckoutDateByResNum(entry.getKey());
            Integer roomId = getRoomIdByHotelIdAndRoomNumber(hotelId, entry.getValue());
            this.roomRepository.updateRoomAvailableDate(roomId, reservationCheckoutDate);
        }
    }

    private LocalDate getReservationCheckoutDateByResNum(int reservationNum) throws EntityNotFoundException {
        Optional<LocalDate> checkoutOpt = reservationRep.findCheckoutDateByReservationNumber(reservationNum);
        checkoutOpt.orElseThrow(() -> new EntityNotFoundException("Reservation" + reservationNum));
        return checkoutOpt.get();
    }

    private Integer getRoomIdByHotelIdAndRoomNumber(int hotelId, int roomNum) throws EntityNotFoundException {
        Optional<Integer> roomIdOpt = roomRepository.findRoomByRoomNumberAndHotelId(hotelId, roomNum);
        roomIdOpt.orElseThrow(() -> new EntityNotFoundException("Room " + roomNum + " in hotel " + hotelId));
        return roomIdOpt.get();
    }
}
