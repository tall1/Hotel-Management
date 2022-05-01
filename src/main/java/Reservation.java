import java.util.HashMap;

public class Reservation {
    private final Integer reservationNumber;
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

    private static int reservationId = 100;
    private HashMap<Request, Boolean> guestsRequests;
    // closed to other rooms


    public Reservation() {
        this.reservationNumber = reservationId++;
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

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationNumber=" + reservationNumber +
                '}';
    }
}
