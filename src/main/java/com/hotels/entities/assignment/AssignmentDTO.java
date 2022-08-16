package com.hotels.entities.assignment;

import com.hotels.entities.reservation.Reservation;
import com.hotels.entities.room.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
@NoArgsConstructor
/* This class is used for returning a simple POJO from AssignmentController: */
public class AssignmentDTO {
    private int hotelId;
    private final Map<Integer, Integer> reservationRoomMap = new HashMap<>();

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }
    public void setReservationRoomMap(Map<Reservation, Room> reservationRoomMap){
        Set<Map.Entry<Reservation, Room>> mapEntrySet = reservationRoomMap.entrySet();
        for (Map.Entry<Reservation, Room> entry : mapEntrySet) {
            int resNum = entry.getKey().getReservationNumber();
            int roomNum = entry.getValue().getRoomNumber();
            this.reservationRoomMap.put(resNum, roomNum);
        }
    }
}
