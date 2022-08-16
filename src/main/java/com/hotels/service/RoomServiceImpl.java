package com.hotels.service;

import com.hotels.entities.feature.Feature;
import com.hotels.entities.hotel.Hotel;
import com.hotels.entities.room.Room;
import com.hotels.entities.room.RoomDTO;
import com.hotels.repository.FeatureRepository;
import com.hotels.repository.HotelRepository;
import com.hotels.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityNotFoundException;
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
    public RoomServiceImpl(RoomRepository roomRepository, HotelRepository hotelRepository, FeatureRepository featureRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.featureRepository = featureRepository;
    }

    @Override
    public List<RoomDTO> getAll() {
        return roomRepository.findAll().
                stream().
                map(this::convertRoomToRoomDto).
                collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public RoomDTO getRoomById(int roomId) {
        checkRoomExists(roomId); // throws exception if not.
        return convertRoomToRoomDto(this.roomRepository.findById(roomId).get());
    }

    @Override
    public void updateRoom(RoomDTO roomDTO) {
        checkRoomExists(roomDTO.getId()); // throws exception if not.
        roomRepository.save(convertRoomDtoToRoom(roomDTO,true));
    }

    @Override
    public void insertRoom(RoomDTO roomDTO) {
        Room room = convertRoomDtoToRoom(roomDTO, false);
        roomRepository.save(room);
    }

    @Override
    public void deleteRoom(int id) {
        checkRoomExists(id); // throws exception if not.
        this.roomRepository.deleteById(id);
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
            if (!featureOpt.isPresent()) {
                throw new EntityNotFoundException("Feature id " + featureId + " not found.");
            }
            featureList.add(featureOpt.get());
        }
        return featureList;
    }

    private Hotel getHotelByHotelId(int hotelId) {
        Optional<Hotel> hotelOpt = this.hotelRepository.findById(hotelId);
        if (!hotelOpt.isPresent()) {
            throw new EntityNotFoundException("Hotel with id " + hotelId + " not found.");
        }
        return hotelOpt.get();
    }

    private void checkRoomExists(int roomId) {
        Optional<Room> roomOpt = this.roomRepository.findById(roomId);
        if(!roomOpt.isPresent()){
            throw new EntityNotFoundException("Room with id " + roomId + " not found");
        }
    }
}
