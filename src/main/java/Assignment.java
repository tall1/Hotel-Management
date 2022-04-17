import java.util.HashMap;
import java.util.Map;

public class Assignment {
    private Map<Room, Reservation> roomReservationHashMap = new HashMap<>();

//    private int roomNo;
//    private int reservationRoomNo;

    public Assignment(Lobby lobby) {
        for (Room room : lobby.getAvailableRoomList()) {
            roomReservationHashMap.put(room, null);
        }
    }

    public Assignment(Map<Room, Reservation> roomReservationHashMap) {
        this.roomReservationHashMap = roomReservationHashMap;
    }

    public void assign(Room room, Reservation reservation){
        if(roomReservationHashMap.containsKey(room)){
            roomReservationHashMap.put(room,reservation);
        }
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "roomReservationHashMap=" + roomReservationHashMap +
                '}';
    }
}
