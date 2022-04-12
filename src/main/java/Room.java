


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

    private Integer roomNumber;
    private RoomFacingDirection roomDirection;
    private Integer roomCapacity;

    private RoomType roomType;
    private Boolean  elevatorProximity;
    private Boolean  hasBathtub;
    private Boolean hasBalcony;

    public Room(){
        roomNumber = counter++;
    }





}
