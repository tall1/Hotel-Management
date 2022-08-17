package com.hotels.service;

import com.hotels.entities.feature.Feature;
import com.hotels.entities.hotel.Hotel;
import com.hotels.entities.reservation.Reservation;
import com.hotels.entities.reservation.ReservationDTO;
import com.hotels.entities.roomreservationfeature.ReservationFeature;
import com.hotels.repository.FeatureRepository;
import com.hotels.repository.HotelRepository;
import com.hotels.repository.ReservationFeatureRepository;
import com.hotels.repository.ReservationRepository;
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
    public ReservationServiceImpl(ReservationRepository reservationRepository, HotelRepository hotelRepository, FeatureRepository featureRepository, ReservationFeatureRepository reservationFeatureRepository) {
        this.reservationRepository = reservationRepository;
        this.hotelRepository = hotelRepository;
        this.featureRepository = featureRepository;
        this.reservationFeatureRepository = reservationFeatureRepository;
    }

    @Override
    public List<ReservationDTO> getAll() {
        return reservationRepository.findAll().
                stream().
                map(this::convertReservationToReservationDto).
                collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public ReservationDTO getReservationByReservationNum(int resNum) {
        checkValidREservationNumber(resNum); // if not - throws exception
        return convertReservationToReservationDto(reservationRepository.findById(resNum).get());
    }

    @Override
    public void insertReservation(ReservationDTO reservationDTO) {
        Optional<Hotel> hotelOpt = this.hotelRepository.findById(reservationDTO.getHotelId());
        if (!hotelOpt.isPresent()) {
            throw new EntityNotFoundException("Hotel with id " + reservationDTO.getHotelId() + "not found.");
        }
        Reservation reservation = createReservationFromReservationDto(reservationDTO, false);
        this.reservationRepository.save(reservation);
        this.reservationFeatureRepository.saveAll(reservation.getReservationFeatures());
    }

    @Override
    public void updateReservation(ReservationDTO reservationDTO) {
        checkValidREservationNumber(reservationDTO.getReservationNumber()); // if not - throws exception
        Reservation reservation = createReservationFromReservationDto(reservationDTO, true);
        this.reservationRepository.save(reservation);
        this.reservationFeatureRepository.saveAll(reservation.getReservationFeatures());
    }

    @Override
    public void deleteReservation(Integer resNum) {
        //checkValidREservationNumber(resNum); // if not - throws exception
        this.reservationRepository.deleteById(resNum);
        this.reservationFeatureRepository.deleteReservationFeatureByReservationNumber(resNum);
    }

    private void checkValidREservationNumber(int reservationNum) {
        Optional<Reservation> reservationOpt = this.reservationRepository.findById(reservationNum);
        if (!reservationOpt.isPresent()) {
            throw new EntityNotFoundException("Reservation number: " + reservationNum + " not found.");
        }
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
    private Reservation createReservationFromReservationDto(ReservationDTO reservationDTO, boolean setReservationNumber) {
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

        for (int i = 1; i <= reservationDTO.getImportanceList().size(); i++) {
            Optional<Feature> featureOptional = this.featureRepository.findById(i);
            int finalI = i;
            featureOptional.orElseThrow(()-> new EntityNotFoundException("Feature with id: " + finalI + " not found."));
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


