import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import java.lang.reflect.Array;
import java.util.*;

public class AssignmentFactory extends AbstractCandidateFactory<Assignment> {

    private List<Room> rooms;
    private List<Reservation> reservations;


/*
    public AssignmentFactory(char[] alphabet, int stringLength) {
        this.alphabet = (char[])alphabet.clone();
        this.stringLength = stringLength;
    }
*/

    public AssignmentFactory(List<Room> rooms, List<Reservation> reservations) {
        if (this.rooms == null){
            this.rooms = new ArrayList<>();
        }
        if (this.reservations == null){
            this.reservations = new ArrayList<>();
        }
//        this.rooms = (List<Room>) ((ArrayList<Room>) rooms).clone();
//        this.reservations = (List<Reservation>) ((ArrayList<Reservation>) reservations).clone();
        rooms.stream().forEach(r->this.rooms.add(r));
        reservations.stream().forEach(r->this.reservations.add(r));
        System.out.println("999");
    }

    public Assignment generateRandomCandidate(Random rng) {
        /*char[] chars = new char[this.stringLength];

        for (int i = 0; i < this.stringLength; ++i) {
            chars[i] = this.alphabet[rng.nextInt(this.alphabet.length)];
        }

        return new String(chars);*/

      /*  Map<Room,Reservation> assignmentSuggestion = new HashMap<>();
        for(Room room: rooms){
            assignmentSuggestion.put(room, reservations.get(rng.nextInt()))
        }*/

        int randomRoomNo = rng.nextInt(this.rooms.size()-1);
        int randomReservationNo = rng.nextInt(this.reservations.size()-1);
        return new Assignment(randomRoomNo, randomReservationNo);

    }
}
