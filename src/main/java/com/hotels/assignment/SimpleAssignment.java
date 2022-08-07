package com.hotels.assignment;

import com.hotels.entities.roomreservationfeature.Reservation;
import com.hotels.entities.roomreservationfeature.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
@NoArgsConstructor
/* This class is used for returning a simple POJO from AssignmentController: */
public class SimpleAssignment {
    private final Map<Integer, Integer> reservationRoomHashMap = new HashMap<>();

    public SimpleAssignment(Assignment asmt) {
        Set<Map.Entry<Reservation, Room>> mapEntrySet = asmt.getReservationRoomHashMap().entrySet();
        for (Map.Entry<Reservation, Room> entry : mapEntrySet) {
            int resNum = entry.getKey().getReservationNumber();
            int roomNum = entry.getValue().getRoomNumber();
            reservationRoomHashMap.put(resNum, roomNum);
        }
    }
}
