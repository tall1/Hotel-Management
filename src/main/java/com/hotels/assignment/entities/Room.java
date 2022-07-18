package com.hotels.assignment.entities;

import com.hotels.assignment.entities.enums.Request;

import java.util.HashMap;
import java.util.Map;

public class Room {
    private static int counter = 1;
    private final int roomNumber;
    private final int roomCapacity;
    private boolean isAvailable;

    private Map<Request, Boolean> requestsMap;

    //private RoomType roomType;
    //private RoomFacingDirection roomDirection;

    /* public enum RoomFacingDirection {
        North,
        South,
        West,
        East
    }*/

    public enum RoomType { // Important?
        Deluxe,
        Executive,
        Club,
        HanndicappedFriendly,
        DeluxeSuite,
        ExecutiveSuite,
        ClubSuite,
        PresidentSuite
    }

    public Room(int roomCapacity) {
        roomNumber = counter++;
        this.roomCapacity = roomCapacity;
        this.requestsMap = new HashMap<>();
        for (Request request : Request.values()) {
            requestsMap.put(request, Math.random() > 0.5);
        }
    }

    public Room(int roomCapacity, Map<Request, Boolean> requestsMap) {
        roomNumber = counter++;
        this.roomCapacity = roomCapacity;
        // Put requestMap in this.requests
    }

    public Boolean doesComplyWithRequest(Request request) {
        return this.requestsMap.get(request);
    }

    public Boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable() {
        this.isAvailable = !this.isAvailable;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    @Override
    public String toString() {
        return "com.hotels.assignment.entities.Room{" +
                "roomNumber=" + roomNumber +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
