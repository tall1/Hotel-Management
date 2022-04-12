import org.uncommons.watchmaker.framework.EvolutionEngine;

import java.util.HashMap;

public class Reservation {
    private final Integer requestedRoomNo;
    private Integer reservationNumber;
    private String guestName;
    private Integer guestId;
    private Integer guestsAmount;
    public enum Request {
        ELEVATORPROXIMITY,
        SEAVIEW,
        BATH,
        BALCONY,
        HANDICAPPED,
        HIGHFLOOR,
        LOWFLOOR
    }
    private static int reservationId = 1;
    private HashMap<Request,Boolean> guestsRequests;
    // closed to other rooms


    public Reservation(Integer requestedRoomNo) {
        this.reservationNumber = reservationId++;
        this.requestedRoomNo = requestedRoomNo;
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

    public HashMap<Request, Boolean> getGuestsRequests() {
        return guestsRequests;
    }
}
