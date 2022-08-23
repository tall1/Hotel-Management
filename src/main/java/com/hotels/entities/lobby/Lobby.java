package com.hotels.entities.lobby;

import com.hotels.entities.reservation.Reservation;
import com.hotels.entities.room.Room;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@EqualsAndHashCode
@ToString
public class Lobby {
    private final List<Room> roomList = new ArrayList<>();
    private final List<Reservation> reservationList = new ArrayList<>();

    public Lobby(List<Room> roomList, List<Reservation> reservationList) {
        this.roomList.addAll(roomList);
        this.reservationList.addAll(reservationList);
    }

    public Room getRandomRoom() {
        if (this.roomList.size() == 0) {
            return null;
        }
        int rnd = getRandomNumberUsingNextInt(0, this.roomList.size());
        return roomList.get(rnd);
    }

    private int getRandomNumberUsingNextInt(int min, int max) {
        // min - inclusive, max - exclusive
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public int getAmountOfRooms() {
        return this.roomList.size();
    }

    public int getAmountOfReservations() {
        return this.reservationList.size();
    }

}
