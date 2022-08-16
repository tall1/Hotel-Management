package com.hotels.service;

import com.hotels.entities.hotel.Hotel;
import com.hotels.entities.hotel.HotelDTO;
import com.hotels.entities.user.User;
import com.hotels.repository.HotelRepository;
import com.hotels.repository.ReservationRepository;
import com.hotels.repository.RoomRepository;
import com.hotels.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    @PostConstruct
    public void init453534() {
        // here put any after construction operations
        System.out.println("HotelServiceImpl: @PostConstruct");
    }

    @PreDestroy
    public void exitAll123() {
        System.out.println("HotelServiceImpl: @PreDestroy");
    }

    @Autowired
    public HotelServiceImpl(HotelRepository hotelRepository, UserRepository userRepository, RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.hotelRepository = hotelRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<HotelDTO> getAll() {
        return hotelRepository.findAll().
                stream().
                map(this::convertHotelToDto).
                collect(Collectors.toList());
    }

    @Override
    public HotelDTO getHotelById(int id) {
        Optional<Hotel> hotelOpt = hotelRepository.findById(id);
        if (hotelOpt.isPresent()) {
            return convertHotelToDto(hotelOpt.get());
        }
        throw new EntityNotFoundException("Hotel " + id + " not found.");
    }

    @Override
    public void updateHotel(HotelDTO hotelDTO) {
        if (!this.hotelRepository.findById(hotelDTO.getId()).isPresent()) {
            throw new EntityNotFoundException("Hotel with id " + hotelDTO.getId() + " not found.");
        }
        if (!this.userRepository.findById(hotelDTO.getAdminId()).isPresent()) {
            throw new EntityNotFoundException("User with id " + hotelDTO.getAdminId() + " not found.");
        }
        Hotel hotel = createHotelFromHotelDto(hotelDTO, true);
        hotelRepository.save(hotel);
    }

    @Override
    public void insertHotel(HotelDTO hotelDTO) {
        Optional<User> userOpt = this.userRepository.findById(hotelDTO.getAdminId());
        if (!userOpt.isPresent()) {
            throw new EntityNotFoundException("User with id " + hotelDTO.getAdminId() + " not found.");
        }
        Hotel hotel = createHotelFromHotelDto(hotelDTO, false); // Id is generated automatically
        hotelRepository.save(hotel);
        userOpt.get().setHotel(hotel); // TODO: CHANGE TO HOTELID
        userRepository.save(userOpt.get());
    }

    @Override
    public void deleteHotel(int id) {
        hotelRepository.deleteHotelById(id);
    }

    private HotelDTO convertHotelToDto(Hotel hotel) {
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setId(hotel.getId());
        hotelDTO.setAdminId(hotel.getAdmin().getId());
        hotelDTO.setHotelName(hotel.getHotelName());
        hotelDTO.setNumOfFloors(hotel.getNumOfFloors());
        hotelDTO.setNumOfRooms(hotel.getNumOfRooms());
        return hotelDTO;
    }

    private Hotel createHotelFromHotelDto(HotelDTO hotelDTO, boolean setId) {
        Hotel hotel = new Hotel();
        if (setId) {
            hotel.setId(hotelDTO.getId());
        }
        hotel.setHotelName(hotelDTO.getHotelName());
        hotel.setNumOfFloors(hotelDTO.getNumOfFloors());
        hotel.setNumOfRooms(hotelDTO.getNumOfRooms());

        User admin = getAdmin(hotelDTO);
        hotel.setAdmin(admin);
        hotel.setRooms(this.roomRepository.findRoomsByHotelId(hotelDTO.getId()));
        hotel.setReservations(this.reservationRepository.findReservationsByHotelId(hotelDTO.getId()));
        return hotel;
    }

    private User getAdmin(HotelDTO hotelDTO) {
        Optional<User> adminOpt = this.userRepository.findById(hotelDTO.getAdminId());
        if (!adminOpt.isPresent()) {
            throw new EntityNotFoundException("Admin " + hotelDTO.getAdminId());
        }
        return adminOpt.get();
    }
}
