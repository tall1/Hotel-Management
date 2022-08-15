package com.hotels.service;

import com.hotels.entities.userhotel.Hotel;
import com.hotels.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;

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
    public HotelServiceImpl(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public List<Hotel> getAll() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotel getHotelById(int id) {
        if (hotelRepository.findById(id).isPresent()) {
            return hotelRepository.findById(id).get();
        }
        throw new EntityNotFoundException("Hotel " + id);
    }

    @Override
    public void insertHotel(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    @Override
    public void updateHotel(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    @Override
    public void deleteHotel(int id) {
        hotelRepository.deleteById(id);
    }
}
