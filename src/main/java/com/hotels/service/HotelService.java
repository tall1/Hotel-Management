package com.hotels.service;

import com.hotels.entities.hotel.HotelDTO;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface HotelService {
     List<HotelDTO> getAll() throws EntityNotFoundException;
     HotelDTO getHotelById(int id) throws EntityNotFoundException;
     int insertHotel(HotelDTO hotel) throws EntityNotFoundException;
     void updateHotel(HotelDTO hotel) throws EntityNotFoundException;
     void deleteHotel(int id) throws EntityNotFoundException;
}
