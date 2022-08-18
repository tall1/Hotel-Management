package com.hotels.service;

import com.hotels.entities.hotel.Hotel;
import com.hotels.entities.user.User;
import com.hotels.entities.user.UserDTO;
import com.hotels.exceptions.EmailAlreadyExistsException;
import com.hotels.repository.EngineRepository;
import com.hotels.repository.HotelRepository;
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
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final EngineRepository engineRepository;

    @PostConstruct
    public void init453534() {
        System.out.println("UserServiceImpl: @PostConstruct");
    }

    @PreDestroy
    public void exitAll123() {
        System.out.println("UserServiceImpl: @PreDestroy");
    }

    @Autowired
    public UserServiceImpl(UserRepository userRepository, HotelRepository hotelRepository, EngineRepository engineRepository) {
        this.userRepository = userRepository;
        this.hotelRepository = hotelRepository;
        this.engineRepository = engineRepository;
    }

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll().
                stream().
                map(this::convertUserToDto).
                collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public UserDTO getUserById(int userId) {
        checkValidUserId(userId); // if not - throws exception
        return convertUserToDto(this.userRepository.findById(userId).get());
    }

    @Override
    public int getUserIdByEmail(String email) throws EntityNotFoundException {
        Optional<Integer> userIdOpt = this.userRepository.findUserIdByEmail(email);
        return userIdOpt.orElseThrow(() -> new EntityNotFoundException("User with email: " + email + " not found."));
    }

    @Override
    public boolean verifyEmailPass(String email, String password) {
        return this.userRepository.findAmountOfEmailAndPasswordCombinations(email, password) > 0;
    }

    @Override
    public void insertUser(UserDTO userDTO) {
        if (this.userRepository.findAmountOfEmails(userDTO.getEmail()) > 0) {
            throw new EmailAlreadyExistsException("Email address: " + userDTO.getEmail() + " already exists.");
        }
        userRepository.save(toUser(userDTO, false));
    }


    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void updateUser(UserDTO userDTO) {
        checkValidUserId(userDTO.getId());
        User user = toUser(userDTO, true);
        user.setPassword(this.userRepository.findById(user.getId()).get().getPassword());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(int userId) {
        userRepository.deleteById(userId); // Also deletes hotel.
        engineRepository.deleteEngineByUserId(userId);
    }

    private void checkValidUserId(int userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        userOpt.orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found."));
    }

    private User toUser(UserDTO userDTO, boolean setId) {
        User user = new User();
        if (setId) {
            user.setId(userDTO.getId());
        }
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        /*user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());*/
        user.setHotel(getHotelByHotelId(userDTO.getHotelId()));
        return user;
    }

    private Hotel getHotelByHotelId(int hotelId) {
        Optional<Hotel> hotelOpt = this.hotelRepository.findById(hotelId);
        Hotel hotel = new Hotel();
        hotel.setId(hotelId);
        return hotelOpt.orElse(null);
    }

    private UserDTO convertUserToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        /*userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());*/
        userDTO.setHotelId(user.getHotel().getId());
        return userDTO;
    }
}
