import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;
import java.util.*;

public class AssignmentFactory extends AbstractCandidateFactory<Assignment> {

    private Lobby lobby;

/*
    public AssignmentFactory(char[] alphabet, int stringLength) {
        this.alphabet = (char[])alphabet.clone();
        this.stringLength = stringLength;
    }
*/

    public AssignmentFactory(Lobby lobby) {
        if (lobby == null || !lobby.isInstanciated()) {
            return;
        }
        this.lobby = lobby;
    }

    public Assignment generateRandomCandidate(Random rng) {
        /*char[] chars = new char[this.stringLength];

        for (int i = 0; i < this.stringLength; ++i) {
            chars[i] = this.alphabet[rng.nextInt(this.alphabet.length)];
        }

        return new String(chars);*/

        Map<Room, Reservation> assignmentSuggestion = new HashMap<>();
        /*// Assign random reservation for each room:
        for (Room room : this.lobby.getAvailableRoomList()) {
            assignmentSuggestion.put(room, lobby.getRandomReservation());
        }*/
        // Assign random room for each reservation:
        for (Reservation reservation:lobby.getReservationArrayList()) {
            assignmentSuggestion.put(lobby.getRandomAvailableRoom(), reservation);
        }
//        int randomRoomNo = rng.nextInt(this.rooms.size() - 1);
//        int randomReservationNo = rng.nextInt(this.reservations.size() - 1);
//        return null;
        //return new Assignment(randomRoomNo, randomReservationNo);
        // TODO: Make sure this is ok - assignmentSuggestion isn't canceled.
        return new Assignment(assignmentSuggestion);
    }
}
