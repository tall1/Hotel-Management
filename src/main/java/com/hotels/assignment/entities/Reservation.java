package com.hotels.assignment.entities;

import com.hotels.assignment.entities.enums.Request;
import com.hotels.assignment.entities.enums.RequestImportance;

import java.util.HashMap;

public class Reservation {
    private final Integer reservationNumber;
    private String guestName;
    private Integer guestId;
    private final Integer guestsAmount;

    private static int reservationId = 100;
    private final HashMap<Request, RequestImportance> guestsRequests;
    // closed to other rooms


    public Reservation(int guestsAmount) {
        this.reservationNumber = reservationId++;
        this.guestsAmount = guestsAmount;
        this.guestsRequests = new HashMap<>();
        for (Request request : Request.values()) {
            this.guestsRequests.put(request, Math.random() > 0.33 ? Math.random() > 0.5 ? RequestImportance.MUST : RequestImportance.NICE_TO_HAVE : RequestImportance.NOT_IMPORTANT);
        }
    }

    public RequestImportance getImportance(Request request) {
        return this.guestsRequests.get(request);
    }

    public Integer getReservationNumber() {
        return reservationNumber;
    }

    public String getGuestName() {
        return guestName;
    }

    public Integer getGuestId() {
        return guestId;
    }

    public Integer getGuestsAmount() {
        return guestsAmount;
    }

    @Override
    public String toString() {
        return "com.hotels.assignment.entities.Reservation{" +
                "reservationNumber=" + reservationNumber +
                '}';
    }
}
