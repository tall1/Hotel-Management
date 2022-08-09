package com.hotels.assignment;

import com.hotels.entities.Lobby;
import com.hotels.entities.roomreservationfeature.Reservation;
import com.hotels.entities.roomreservationfeature.Room;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class Assignment {
    private final Map<Reservation, Room> reservationRoomHashMap;
    private final Lobby lobby;

    public Assignment(Lobby lobby, Map<Reservation, Room> reservationRoomHashMap) {
        this.reservationRoomHashMap = new HashMap<>(reservationRoomHashMap);
        this.lobby = lobby;
    }

    public Assignment(Assignment otherAssignment) {
        this.lobby = otherAssignment.lobby;
        this.reservationRoomHashMap = new HashMap<>(otherAssignment.reservationRoomHashMap);
    }

    public Room getRoomByReservation(Reservation reservation) {
        return this.reservationRoomHashMap.get(reservation);
    }

    public void assign(Room room, Reservation reservation) {
        this.reservationRoomHashMap.put(reservation, room);
    }

    public int getAmountOfReservations() {
        return this.lobby.getAmountOfReservations();
    }

    public int getAmountOfRooms() {
        return this.lobby.getAmountOfRooms();
    }

    public Collection<Reservation> getReservations() {
        return this.reservationRoomHashMap.keySet();
    }

    public Map<Room, Integer> getAmountOfReservationsPerRoom() {
        Map<Room, Integer> roomToAmountOfReservationsMap = new HashMap<>(this.getAmountOfRooms());
        for (Room room : this.lobby.getRoomArrayList()) {
            roomToAmountOfReservationsMap.put(room, 0);
        }
        for (Reservation reservation : this.lobby.getReservationArrayList()) {
            Room room = this.reservationRoomHashMap.get(reservation);
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
        List<Reservation> reservationList = new ArrayList<>(this.reservationRoomHashMap.keySet());
        reservationList.sort(Comparator.comparingInt(Reservation::getReservationNumber));

        for (Reservation reservation : reservationList) {
            Room room = reservationRoomHashMap.get(reservation);
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
