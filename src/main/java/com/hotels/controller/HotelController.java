package com.hotels.controller;

import com.hotels.entities.userhotel.Hotel;
import com.hotels.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(path = "/hotels")
public class HotelController {
    private HotelService hotelService;

    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/")
    public List<Hotel> getAll() throws SQLException {
        return this.hotelService.getAll();
    }

//    localhost:8080/api/v1/person/get1/5
    @GetMapping("/{id}")
    public Hotel getHotelById(@PathVariable int id) throws SQLException {
        return hotelService.getHotelById(id);
    }

    /*//http://localhost:8080/api/v1/person/get2?id=5
    @GetMapping
    public Hotel getPersonById2(int id) throws SQLException {
        return hotelService.getPersonById(id);
    }*/

    @PostMapping
    public void insertHotel(Hotel hotel) throws SQLException {
        hotelService.insertHotel(hotel);
    }

    @PutMapping
    public void updateHotel(Hotel hotel) throws SQLException {
        hotelService.updateHotel(hotel);
    }

    @DeleteMapping
    public void deleteHotel(int id) throws SQLException {
        hotelService.deleteHotel(id);
    }
}
