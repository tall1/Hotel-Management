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
import javax.transaction.Transactional;
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
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public HotelDTO getHotelById(int hotelId) {
        checkValidHotelId(hotelId);
        return convertHotelToDto(this.hotelRepository.findById(hotelId).get());
    }

    @Override
    public void updateHotel(HotelDTO hotelDTO) {
        checkValidHotelId(hotelDTO.getId());
        Hotel hotel = convertDtoToHotel(hotelDTO, true);
        hotelRepository.save(hotel);
    }

    @Override
    @Transactional
    public int insertHotel(HotelDTO hotelDTO) {
        Optional<User> userOpt = this.userRepository.findById(hotelDTO.getAdminId());
        userOpt.orElseThrow(() -> new EntityNotFoundException("User with id " + hotelDTO.getAdminId() + " not found."));
        Hotel hotel = convertDtoToHotel(hotelDTO, false); // id is generated automatically
        int newHotelId = hotelRepository.save(hotel).getId();
        userOpt.get().setHotel(hotel);
        userRepository.save(userOpt.get());
        return newHotelId;
    }

    @Override
    @Transactional
    public void deleteHotel(int hotelId) {
        Optional<Hotel> hotel = this.hotelRepository.findById(hotelId);
        if(!hotel.isPresent()){
            return;
        }
        Optional<User> user = this.userRepository.findById(hotel.get().getAdmin().getId());
        user.ifPresent(u -> u.setHotel(null));
        hotelRepository.deleteHotelById(hotelId);
    }

    private void checkValidHotelId(int hotelId) {
        Optional<Hotel> hotelOpt = this.hotelRepository.findById(hotelId);
        hotelOpt.orElseThrow(() -> new EntityNotFoundException("Hotel with id " + hotelId + " not found."));
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

    private Hotel convertDtoToHotel(HotelDTO hotelDTO, boolean setId) {
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
        adminOpt.orElseThrow(() -> new EntityNotFoundException("Admin with id: " + hotelDTO.getAdminId() + " not found."));
        return adminOpt.get();
    }
}
