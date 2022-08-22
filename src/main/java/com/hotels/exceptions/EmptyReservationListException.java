package com.hotels.exceptions;

public class EmptyReservationListException extends RuntimeException {
    public EmptyReservationListException() {
        super("There are no relevant reservations for this date.");
    }
}
