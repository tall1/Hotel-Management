package com.hotels.service;

import com.hotels.entities.feature.Feature;
import com.hotels.entities.hotel.Hotel;
import com.hotels.entities.reservation.Reservation;
import com.hotels.entities.reservation.ReservationDTO;
import com.hotels.entities.roomreservationfeature.ReservationFeature;
import com.hotels.repository.*;
import com.hotels.utils.FeatureCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final HotelRepository hotelRepository;
    private final FeatureRepository featureRepository;
    private final ReservationFeatureRepository reservationFeatureRepository;
    private final UserRepository userRepository;

    @PostConstruct
    public void init453534() {
        // here put any after construction operations
        System.out.println("ReservationServiceImpl: @PostConstruct");
    }

    @PreDestroy
    public void exitAll123() {
        System.out.println("ReservationServiceImpl: @PreDestroy");
    }

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, HotelRepository hotelRepository, FeatureRepository featureRepository, ReservationFeatureRepository reservationFeatureRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.hotelRepository = hotelRepository;
        this.featureRepository = featureRepository;
        this.reservationFeatureRepository = reservationFeatureRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ReservationDTO> getAll() {
        return reservationRepository.findAll().
                stream().
                map(this::convertReservationToReservationDto).
                collect(Collectors.toList());
    }

    @Override
    public List<ReservationDTO> getAllByUserId(int userId) throws EntityNotFoundException {
        Optional<Integer> hotelId = this.userRepository.findHotelIdByUserId(userId);
        hotelId.orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " not found."));
        return this.reservationRepository
                .findReservationsByHotelId(hotelId.get())
                .stream()
                .map(this::convertReservationToReservationDto)
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public ReservationDTO getReservationByReservationNum(int resNum) {
        checkValidReservationNumberOrElseThrow(resNum); // if not - throws exception
        return convertReservationToReservationDto(reservationRepository.findById(resNum).get());
    }

    @Override
    public int insertReservation(ReservationDTO reservationDTO) throws EntityNotFoundException {
        checkValidHotelOrElseThrow(reservationDTO);
        Reservation reservation = toReservation(reservationDTO, false);
        int newReservationNum = this.reservationRepository.save(reservation).getReservationNumber();
        this.reservationFeatureRepository.saveAll(reservation.getReservationFeatures());
        return newReservationNum;
    }

    @Override
    @Transactional
    public List<Integer> insertReservations(List<ReservationDTO> reservationsDTO) throws EntityNotFoundException {
        List<Reservation> reservations = new ArrayList<>();
        for (ReservationDTO resDTO : reservationsDTO) {
            checkValidHotelOrElseThrow(resDTO);
            Reservation reservation = toReservation(resDTO, false);
            reservations.add(reservation);
        }
        List<Integer> resNumList = this.reservationRepository.saveAll(reservations).
                stream().
                map(Reservation::getReservationNumber).
                collect(Collectors.toList());
        for (Reservation reservation : reservations) {
            this.reservationFeatureRepository.saveAll(reservation.getReservationFeatures());
        }
        return resNumList;
    }

    @Override
    public void updateReservation(ReservationDTO reservationDTO) {
        checkValidReservationNumberOrElseThrow(reservationDTO.getReservationNumber()); // if not - throws exception
        Reservation reservation = toReservation(reservationDTO, true);
        this.reservationRepository.save(reservation);
        this.reservationFeatureRepository.saveAll(reservation.getReservationFeatures());
    }

    @Override
    public void deleteReservation(Integer resNum) {
        this.reservationRepository.deleteReservationByReservationNumber(resNum);
        this.reservationFeatureRepository.deleteReservationFeatureByReservationNumber(resNum);
    }

    private void checkValidReservationNumberOrElseThrow(int reservationNum) {
        Optional<Reservation> reservationOpt = this.reservationRepository.findById(reservationNum);
        reservationOpt.orElseThrow(() -> new EntityNotFoundException("Reservation number: " + reservationNum + " not found."));
    }

    private void checkValidHotelOrElseThrow(ReservationDTO resDTO) {
        Optional<Hotel> hotelOpt = this.hotelRepository.findById(resDTO.getHotelId());
        hotelOpt.orElseThrow(() -> new EntityNotFoundException("Hotel with id " + resDTO.getHotelId() + "not found."));
    }

    private ReservationDTO convertReservationToReservationDto(Reservation reservation) {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setReservationNumber(reservation.getReservationNumber());
        reservationDTO.setHotelId(reservation.getHotel().getId());
        reservationDTO.setGuestName(reservation.getGuestName());
        reservationDTO.setGuestsAmount(reservation.getGuestsAmount());
        reservationDTO.setCheckin(reservation.getCheckin().toString());
        reservationDTO.setCheckout(reservation.getCheckout().toString());
        reservationDTO.setImportanceListByReservationFeatureList(reservation.getReservationFeatures());
        return reservationDTO;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private Reservation toReservation(ReservationDTO reservationDTO, boolean setReservationNumber) {
        Reservation reservation = new Reservation();
        if (setReservationNumber) {
            reservation.setReservationNumber(reservationDTO.getReservationNumber());
        }
        reservation.setHotel(this.hotelRepository.findById(reservationDTO.getHotelId()).get());
        reservation.setGuestName(reservationDTO.getGuestName());
        reservation.setGuestsAmount(reservationDTO.getGuestsAmount());
        reservation.setCheckin(LocalDate.parse(reservationDTO.getCheckin()));
        reservation.setCheckout(LocalDate.parse(reservationDTO.getCheckout()));
        reservation.setReservationFeatures(createReservationFeatureListFromReservationDto(reservationDTO, reservation, setReservationNumber));
        return reservation;
    }

    private List<ReservationFeature> createReservationFeatureListFromReservationDto(ReservationDTO reservationDTO, Reservation reservation, boolean setId) {
        List<ReservationFeature> reservationFeatures = new ArrayList<>();

        for (int i = 1; i <= FeatureCounter.getAmountOfFeatures(); i++) {
            Optional<Feature> featureOptional = this.featureRepository.findById(i);
            int finalI = i;
            featureOptional.orElseThrow(() -> new EntityNotFoundException("Feature with id: " + finalI + " not found."));
            ReservationFeature reservationFeature = new ReservationFeature();
            if (setId) {
                reservationFeature.setId(this.reservationFeatureRepository.findReservationFeatureIdByResNumAndFeatureId(reservationDTO.getReservationNumber(), i));
            }
            reservationFeature.setReservation(reservation);
            reservationFeature.setFeature(featureOptional.get());
            reservationFeature.setImportance(reservationDTO.getImportanceList().get(i));
            reservationFeatures.add(reservationFeature);
        }
        return reservationFeatures;
    }
}


