package com.hotels.service;
import com.hotels.entities.roomreservationfeature.Reservation;
import com.hotels.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;

    @PostConstruct
    public void init453534(){
        // here put any after construction operations
        System.out.println("ReservationServiceImpl: @PostConstruct");
    }
    @PreDestroy
    public void  exitAll123(){
        System.out.println("ReservationServiceImpl: @PreDestroy");
    }

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation getReservationById(int id)  {
        if(reservationRepository.findById(id).isPresent()){
            return reservationRepository.findById(id).get();
        }
        throw new EntityNotFoundException("Reservation " + id);
    }

    @Override
    public void insertReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Override
    public void updateReservation(Reservation reservation){
        reservationRepository.save(reservation);
    }

    @Override
    public void deleteReservation(Integer resNum) {
        reservationRepository.deleteReservationByReservationNumber(resNum);
    }
}
