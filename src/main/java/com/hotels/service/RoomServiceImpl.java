package com.hotels.service;

import com.hotels.entities.feature.Feature;
import com.hotels.entities.hotel.Hotel;
import com.hotels.entities.room.Room;
import com.hotels.entities.room.RoomDTO;
import com.hotels.repository.FeatureRepository;
import com.hotels.repository.HotelRepository;
import com.hotels.repository.RoomRepository;
import com.hotels.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityNotFoundException;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final FeatureRepository featureRepository;
    private final UserRepository userRepository;
    private final DataSource dataSource;

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
    public RoomServiceImpl(RoomRepository roomRepository, HotelRepository hotelRepository, FeatureRepository featureRepository, DataSource dataSource, UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.featureRepository = featureRepository;
        this.dataSource = dataSource;
        this.userRepository = userRepository;
    }

    @Override
    public List<RoomDTO> getAll() {
        return roomRepository.findAll().
                stream().
                map(this::convertRoomToRoomDto).
                collect(Collectors.toList());
    }

    @Override
    public List<RoomDTO> getAllByUserId(int userId) throws EntityNotFoundException {
        Optional<Integer> hotelId = this.userRepository.findHotelIdByUserId(userId);
        hotelId.orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " not found."));
        return this.roomRepository
                .findRoomsByHotelId(hotelId.get())
                .stream()
                .map(this::convertRoomToRoomDto)
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public RoomDTO getRoomById(int roomId) {
        checkValidRoomId(roomId); // throws exception if not.
        return convertRoomToRoomDto(this.roomRepository.findById(roomId).get());
    }

    @Override
    public int insertRoom(RoomDTO roomDTO) {
        Room room = convertRoomDtoToRoom(roomDTO, false);
        return roomRepository.save(room).getId();
    }

    @Override
    public List<Integer> insertRooms(List<RoomDTO> roomsDTOs) throws EntityNotFoundException {
        List<Room> roomsList = new ArrayList<>();
        for (RoomDTO roomDTO : roomsDTOs) {
            roomsList.add(convertRoomDtoToRoom(roomDTO, false));
        }
        return this.roomRepository.
                saveAll(roomsList).
                stream().
                map(Room::getId).
                collect(Collectors.toList());
    }

    @Override
    public void updateRoom(RoomDTO roomDTO) {
        checkValidRoomId(roomDTO.getId()); // throws exception if not.
        roomRepository.save(convertRoomDtoToRoom(roomDTO, true));
    }

    @Override
    @Transactional
    public void deleteRoom(int roomId) throws SQLException {
        this.roomRepository.deleteRoomByRoomId(roomId);
        deleteFromRelationTable(roomId);
    }

    private void deleteFromRelationTable(int roomId) throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("delete from room_feature where room_id = " + roomId);
    }

    private RoomDTO convertRoomToRoomDto(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setRoomNumber(room.getRoomNumber());
        roomDTO.setHotelId(room.getHotel().getId());
        roomDTO.setFloorNumber(room.getFloorNumber());
        roomDTO.setRoomCapacity(room.getRoomCapacity());
        roomDTO.setAvailableDate(room.getAvailableDate().toString());
        roomDTO.setFeatureIdsListByFeatureList(room.getFeatureList());
        return roomDTO;
    }

    private Room convertRoomDtoToRoom(RoomDTO roomDTO, boolean setId) {
        Room room = new Room();
        if (setId) {
            room.setId(roomDTO.getId());
        }
        room.setRoomNumber(roomDTO.getRoomNumber());
        room.setHotel(getHotelByHotelId(roomDTO.getHotelId()));
        room.setFloorNumber(roomDTO.getFloorNumber());
        room.setRoomCapacity(roomDTO.getRoomCapacity());
        room.setAvailableDate(LocalDate.parse(roomDTO.getAvailableDate()));
        room.setFeatureList(convertFeatureIdListToFeatureList(roomDTO.getFeatureIdsList()));
        return room;
    }

    private List<Feature> convertFeatureIdListToFeatureList(List<Integer> featureIdList) {
        List<Feature> featureList = new ArrayList<>();
        List<Feature> allFeatures = this.featureRepository.findAll();
        allFeatures.sort(Comparator.comparingInt(Feature::getId));
        for (Integer featureId : featureIdList) {
            Optional<Feature> featureOpt = this.featureRepository.findById(featureId);
            featureOpt.orElseThrow(() -> new EntityNotFoundException("Feature id " + featureId + " not found."));
            featureList.add(featureOpt.get());
        }
        return featureList;
    }

    private Hotel getHotelByHotelId(int hotelId) {
        Optional<Hotel> hotelOpt = this.hotelRepository.findById(hotelId);
        hotelOpt.orElseThrow(() -> new EntityNotFoundException("Hotel with id " + hotelId + " not found."));
        return hotelOpt.get();
    }

    private void checkValidRoomId(int roomId) {
        Optional<Room> roomOpt = this.roomRepository.findById(roomId);
        roomOpt.orElseThrow(() -> new EntityNotFoundException("Room with id " + roomId + " not found"));
    }
}
