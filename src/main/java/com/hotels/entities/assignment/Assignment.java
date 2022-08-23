package com.hotels.entities.assignment;

import com.hotels.entities.lobby.Lobby;
import com.hotels.entities.reservation.Reservation;
import com.hotels.entities.room.Room;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class Assignment {
    private final Map<Reservation, Room> reservationRoomMap;
    private final Lobby lobby;

    public Assignment(Lobby lobby, Map<Reservation, Room> reservationRoomMap) {
        this.reservationRoomMap = new HashMap<>(reservationRoomMap);
        this.lobby = lobby;
    }

    public Assignment(Assignment otherAssignment) {
        this.lobby = otherAssignment.lobby;
        this.reservationRoomMap = new HashMap<>(otherAssignment.reservationRoomMap);
    }

    public Room getRoomByReservation(Reservation reservation) {
        return this.reservationRoomMap.get(reservation);
    }

    public void assign(Room room, Reservation reservation) {
        this.reservationRoomMap.put(reservation, room);
    }

    public int getAmountOfReservations() {
        return this.lobby.getAmountOfReservations();
    }

    public int getAmountOfRooms() {
        return this.lobby.getAmountOfRooms();
    }

    public Collection<Reservation> getReservations() {
        return this.reservationRoomMap.keySet();
    }

    public Map<Room, Integer> getAmountOfReservationsPerRoom() {
        Map<Room, Integer> roomToAmountOfReservationsMap = new HashMap<>(this.getAmountOfRooms());
        for (Room room : this.lobby.getRoomList()) {
            roomToAmountOfReservationsMap.put(room, 0);
        }
        for (Reservation reservation : this.lobby.getReservationList()) {
            Room room = this.reservationRoomMap.get(reservation);
            Integer currentReservationAmount = roomToAmountOfReservationsMap.get(room);
            roomToAmountOfReservationsMap.put(room, currentReservationAmount + 1);
        }
        return roomToAmountOfReservationsMap;
    }

    public int getLobbyHashcode() {
        return this.lobby.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder assignments = new StringBuilder();
        List<Reservation> reservationList = new ArrayList<>(this.reservationRoomMap.keySet());
        reservationList.sort(Comparator.comparingInt(Reservation::getReservationNumber));

        for (Reservation reservation : reservationList) {
            Room room = reservationRoomMap.get(reservation);
            assignments.
                    append("Reservation: ").
                    append(reservation.getReservationNumber()).
                    append(" - Room: ").append(room.getRoomNumber()).
                    append("\n");
        }
        return "Assignment{\n" +
                assignments +
                '}';
    }


}
