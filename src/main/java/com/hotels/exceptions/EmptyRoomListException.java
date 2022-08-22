package com.hotels.exceptions;

public class EmptyRoomListException extends RuntimeException {
    public EmptyRoomListException() {
        super("There are no available rooms for this date.");
    }
}
