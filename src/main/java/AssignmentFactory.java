import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import java.util.*;

public class AssignmentFactory extends AbstractCandidateFactory<Assignment> {

    private Lobby lobby;

    public AssignmentFactory(Lobby lobby) {
        if (lobby == null || !lobby.isInstantiated()) {
            return;
        }
        this.lobby = lobby;
    }

    public Assignment generateRandomCandidate(Random rng) {
        Map<Reservation, Room> assignmentSuggestion = new HashMap<>();
        Room room;
        // Assign random room for each reservation:
        for (Reservation reservation : lobby.getReservationArrayList()) {
            room = lobby.getRandomRoom();
            assignmentSuggestion.put(reservation, room);
            // Enable multiple reservations per room:
            if(room.isAvailable()){
                room.setAvailable();
                lobby.addOrRemoveRoomFromAvailable(room);
            }
        }
        return new Assignment(lobby, assignmentSuggestion);
    }
}
