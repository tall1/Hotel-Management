package com.hotels.entities.hotel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HotelDTO {
    private int id;
    private int adminId;
    private String hotelName;
    private int numOfFloors;
    private int numOfRooms;
}
