import java.util.HashMap;

public class Reservation {
    private final Integer reservationNumber;
    private String guestName;
    private Integer guestId;
    private Integer guestsAmount;

    private static int reservationId = 100;
    private HashMap<Requests, RequestImportance> guestsRequests;
    // closed to other rooms


    public Reservation() {
        this.reservationNumber = reservationId++;
    }

    public RequestImportance getImportance(Requests request){
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

    public HashMap<Requests, RequestImportance> getGuestsRequests() {
        return guestsRequests;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationNumber=" + reservationNumber +
                '}';
    }
}
