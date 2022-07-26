package com.hotels.entities;

import com.hotels.entities.roomreservationfeature.Reservation;
import com.hotels.entities.roomreservationfeature.Room;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Lobby {
    private final ArrayList<Room> roomArrayList = new ArrayList<>();
    private final ArrayList<Reservation> reservationArrayList = new ArrayList<>();
    private final ArrayList<Room> availableRoomList = new ArrayList<>();

    // TODO: Add a constructor from an XML or something...

    public Lobby(ArrayList<Room> roomArrayList, ArrayList<Reservation> reservationArrayList) {
        // Note: This is not a hard copy.
        this.roomArrayList.addAll(roomArrayList);
        this.reservationArrayList.addAll(reservationArrayList);
        // Add references to available rooms:
        roomArrayList.forEach(r -> {
            if (r.getIsAvailable()) {
                this.availableRoomList.add(r);
            }
        });
    }

    public ArrayList<Room> getRoomArrayList() {
        return roomArrayList;
    }

    public ArrayList<Reservation> getReservationArrayList() {
        return reservationArrayList;
    }

    public Room getRandomRoom() {
        if (this.roomArrayList.size() == 0) {
            return null;
        }
        int rnd = getRandomNumberUsingNextInt(0, this.roomArrayList.size());
        return roomArrayList.get(rnd);
    }

    public void addOrRemoveRoomFromAvailable(Room room) {
        if (availableRoomList.contains(room)) {
            availableRoomList.remove(room);
        } else {
            availableRoomList.add(room);
        }
    }

    private int getRandomNumberUsingNextInt(int min, int max) {
        // min - inclusive, max - exclusive
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public int getAmountOfRooms() {
        return this.roomArrayList.size();
    }

    public int getAmountOfReservations() {
        return this.reservationArrayList.size();
    }

    @Override
    public String toString() {
        return "com.hotels.assignment.entities.Lobby{" +
                "roomArrayList=" + roomArrayList +
                ", reservationArrayList=" + reservationArrayList +
                ", availableRoomList=" + availableRoomList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lobby lobby = (Lobby) o;
        return Objects.equals(roomArrayList, lobby.roomArrayList) && Objects.equals(reservationArrayList, lobby.reservationArrayList) && Objects.equals(availableRoomList, lobby.availableRoomList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomArrayList, reservationArrayList, availableRoomList);
    }
}
