import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
    private static int counter = 1;
    private final int roomNumber;
    private int roomCapacity;
    private boolean isAvailable;
    //private Integer availableNights; // available nights from now.
    HashMap<Requests, Boolean> requestsMap;

    //private RoomType roomType;
    //private RoomFacingDirection roomDirection;

    /*public enum RoomFacingDirection {
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
        PresidentSuite;
    }

    public Room(int roomCapacity, Map<Requests, Boolean> requestsMap) {
        roomNumber = counter++;
        this.roomCapacity = roomCapacity;
        // Put requestMap in this.requests
    }

    public Boolean doesComplyWithRequest(Requests request) {
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
        return "Room{" +
                "roomNumber=" + roomNumber +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
