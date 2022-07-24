package com.hotels.service;

import com.hotels.entities.userhotel.Hotel;

import java.sql.SQLException;
import java.util.List;

public interface HotelService {
     List<Hotel> getAll() throws SQLException;
     Hotel getHotelById(int id) throws SQLException;
     void insertHotel(Hotel room) throws SQLException;
     void updateHotel(Hotel room) throws SQLException;
     void deleteHotel(int id) throws SQLException;
}
