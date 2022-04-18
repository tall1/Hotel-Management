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
        Room room;
        // Assign random room for each reservation:
        for (Reservation reservation : lobby.getReservationArrayList()) {
            room = lobby.getRandomAvailableRoom();
            assignmentSuggestion.put(room, reservation);
            room.setAvailable();
            lobby.addOrRemoveRoomFromAvailable(room);
        }
        return new Assignment(assignmentSuggestion);
    }
}
