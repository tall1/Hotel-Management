import java.util.HashMap;
import java.util.Map;

public class Assignment {
    //private Map<Room, Reservation> roomReservationHashMap = new HashMap<>();

    private int roomNo;
    private int reservationRoomNo;

    public Assignment(int roomNo, int reservationRoomNo) {
        this.roomNo = roomNo;
        this.reservationRoomNo = reservationRoomNo;
    }

   /* public Assignment(Map<Room, Reservation> roomReservationHashMap) {
        for (Room room :
                roomReservationHashMap.keySet()) {
            this.roomReservationHashMap.put(room, roomReservationHashMap.get(room));
        }
    }*/

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public int getReservationRoomNo() {
        return reservationRoomNo;
    }

    public void setReservationRoomNo(int reservationRoomNo) {
        this.reservationRoomNo = reservationRoomNo;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "roomNo=" + roomNo +
                ", reservationRoomNo=" + reservationRoomNo +
                '}';
    }
}
