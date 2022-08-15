package com.hotels.service;

import com.hotels.entities.userhotel.Hotel;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface HotelService {
     List<Hotel> getAll() throws EntityNotFoundException;
     Hotel getHotelById(int id) throws EntityNotFoundException;
     void insertHotel(Hotel hotel) throws EntityNotFoundException;
     void updateHotel(Hotel hotel) throws EntityNotFoundException;
     void deleteHotel(int id) throws EntityNotFoundException;
}
