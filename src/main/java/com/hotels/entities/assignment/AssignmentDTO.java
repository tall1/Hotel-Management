package com.hotels.entities.assignment;

import com.hotels.entities.reservation.Reservation;
import com.hotels.entities.room.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class AssignmentDTO {
    private int hotelId;
    private final Map<Integer, Integer> reservationRoomMap = new HashMap<>();

    public void copyReservationAndRoomIds(Map<Reservation, Room> reservationRoomMap) {
        Set<Map.Entry<Reservation, Room>> mapEntrySet = reservationRoomMap.entrySet();
        for (Map.Entry<Reservation, Room> entry : mapEntrySet) {
            int resNum = entry.getKey().getReservationNumber();
            int roomNum = entry.getValue().getRoomNumber();
            this.reservationRoomMap.put(resNum, roomNum);
        }
    }
}
