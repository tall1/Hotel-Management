import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Lobby {
    private static Lobby singleInstanceLobby = null;
    private final ArrayList<Room> roomArrayList = new ArrayList<>();
    private final ArrayList<Reservation> reservationArrayList = new ArrayList<>();
    private final ArrayList<Room> availableRoomList = new ArrayList<>();

    // TODO: Add a constructor from an XML or something...

    private Lobby(ArrayList<Room> roomArrayList, ArrayList<Reservation> reservationArrayList) {
        // Note: This is not a hard copy.
        this.roomArrayList.addAll(roomArrayList);
        this.reservationArrayList.addAll(reservationArrayList);
        // Add references to available rooms:
        roomArrayList.forEach(r -> {
            if (r.isAvailable()) {
                this.availableRoomList.add(r);
            }
        });
    }

    public static Lobby getInstance() {
        if (singleInstanceLobby == null) {
            throw new IllegalArgumentException("No instance of Lobby initiated.");
        }
        return singleInstanceLobby;
    }

    public static Lobby getInstance(ArrayList<Room> roomArrayList, ArrayList<Reservation> reservationArrayList) {
        if (singleInstanceLobby == null) {
            singleInstanceLobby = new Lobby(roomArrayList, reservationArrayList);
        }
        return singleInstanceLobby;
    }

//    TODO:Later we need to load from XML:
//    public static Lobby getInstance(String xml_url) {
//        if (singleInstanceLobby == null) {
//            singleInstanceLobby = new Lobby(xml_url);
//        }
//        return singleInstanceLobby;
//    }

    public boolean isInstantiated() {
        return singleInstanceLobby != null;
    }

    public ArrayList<Room> getRoomArrayList() {
        return roomArrayList;
    }

    public ArrayList<Reservation> getReservationArrayList() {
        return reservationArrayList;
    }

    public Reservation getRandomReservation() {
        if (this.reservationArrayList.size() == 0) {
            return null;
        }
        int rnd = getRandomNumberUsingNextInt(0, this.reservationArrayList.size());
        return reservationArrayList.get(rnd);
    }

    public Room getRandomAvailableRoom() {
        if (this.availableRoomList.size() == 0) {
            return null;
        }
        int rnd = getRandomNumberUsingNextInt(0, this.availableRoomList.size());
        return availableRoomList.get(rnd);
    }

    public Room getRandomRoom() {
        if (this.roomArrayList.size() == 0) {
            return null;
        }
        int rnd = getRandomNumberUsingNextInt(0, this.roomArrayList.size());
        return roomArrayList.get(rnd);
    }

    public void addOrRemoveRoomFromAvailable(Room room) {
        if(availableRoomList.contains(room)){
            availableRoomList.remove(room);
        }else{
            availableRoomList.add(room);
        }
    }

    private int getRandomNumberUsingNextInt(int min, int max) {
        // min - inclusive, max - exclusive
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    @Override
    public String toString() {
        return "Lobby{" +
                "roomArrayList=" + roomArrayList +
                ", reservationArrayList=" + reservationArrayList +
                ", availableRoomList=" + availableRoomList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lobby lobby = (Lobby) o;
        return Objects.equals(roomArrayList, lobby.roomArrayList) && Objects.equals(reservationArrayList, lobby.reservationArrayList) && Objects.equals(availableRoomList, lobby.availableRoomList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomArrayList, reservationArrayList, availableRoomList);
    }
}
