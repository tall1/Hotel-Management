import java.util.ArrayList;
import java.util.HashMap;

public class Lobby {
    private ArrayList<Room> roomArrayList;
    private ArrayList<Reservation> reservationArrayList;
//    private HashMap<>
    private Lobby(/* Load a rooms list from an XML or something */) {
        roomArrayList = new ArrayList<>();
        // Here the roomArrayList is filled with rooms
        reservationArrayList = new ArrayList<>();
    }

    public void addReservation(Reservation reservation){
        // TODO: Check that the reservation details are ok. if not - return false or something..
        reservationArrayList.add(reservation);
    }

    public void removeReservation(int reservationNo){
        for (Reservation reservation:
             reservationArrayList) {
            if(reservation.getReservationNumber() == reservationNo){
                reservationArrayList.remove(reservation);
                break;
            }
        }
    }
}
