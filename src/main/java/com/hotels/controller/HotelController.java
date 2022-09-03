package com.hotels.controller;

import com.hotels.entities.hotel.HotelDTO;
import com.hotels.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping(path = "/hotels")
@CrossOrigin(origins = "http://localhost:3000")
public class HotelController {
    private final HotelService hotelService;

    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/")
    public List<HotelDTO> getAll() throws EntityNotFoundException {
        return this.hotelService.getAll();
    }

    //    localhost:8080/api/v1/person/get1/5
    @GetMapping("/{hotelId}")
    public HotelDTO getHotelById(@PathVariable Integer hotelId) throws EntityNotFoundException{
        return hotelService.getHotelById(hotelId);
    }

    /*//http://localhost:8080/api/v1/person/get2?id=5
    @GetMapping
    public Hotel getPersonById2(int id) throws EntityNotFoundException{
        return hotelService.getPersonById(id);
    }*/

    @PostMapping
    public int insertHotel(@RequestBody HotelDTO hotelDTO) throws EntityNotFoundException{
        return hotelService.insertHotel(hotelDTO);
    }

    @PutMapping
    public void updateHotel(@RequestBody HotelDTO hotelDTO) throws EntityNotFoundException{
        hotelService.updateHotel(hotelDTO);
    }

    @DeleteMapping
    public void deleteHotel(int id) throws EntityNotFoundException{
        hotelService.deleteHotel(id);
    }
}
