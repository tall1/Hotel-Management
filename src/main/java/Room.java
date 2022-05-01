


public class Room {
    public enum RoomFacingDirection {
        North,
        South,
        West,
        East
    }

    public enum RoomType {
        Deluxe,
        Executive,
        Club,
        HanndicappedFriendly,
        DeluxeSuite,
        ExecutiveSuite,
        ClubSuite,
        PresidentSuite
    }

    private static int counter = 1;

    private final int roomNumber;
    private RoomFacingDirection roomDirection;
    private RoomType roomType;
    private int roomCapacity;
    private boolean elevatorProximity;
    private boolean hasBathtub;
    private boolean hasBalcony;
    private boolean isAvailable = true;

    // private Integer availableNights; // available nights from now.

    public Room() {
        roomNumber = counter++;
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

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber=" + roomNumber +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
