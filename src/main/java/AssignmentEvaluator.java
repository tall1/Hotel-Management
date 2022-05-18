import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.List;
import java.util.Map;

public class AssignmentEvaluator implements FitnessEvaluator<Assignment> {
    private final String targetString = "HELLO WORLD";

    /**
     * Fines the fitness for every:
     * 1. Room with multiple reservations.
     * 2. Insufficient room.
     * 3. Unfulfilled request, according to the importance.
     */
    public double getFitness(Assignment candidate
            , List<? extends Assignment> population) {

        double fitness = candidate.getMaxFitness();
        // Handle multiple reservations per room:
        Map<Room, Integer> reservationsPerRoom = candidate.getAmountOfReservationsPerRoom();
        for (Integer amountOfReservations : reservationsPerRoom.values()) {
            if (amountOfReservations > 1) {
                // TODO: Do something to fitness..
            }
        }
        // Handle more guests than room capacity:
        int amountOfInsufficientRooms = candidate.getAmountOfInsufficientRooms();
        for (int i = 0; i < amountOfInsufficientRooms; i++) {
            // TODO: Do something to fitness..
        }
        // Handle unfulfilled requests:
        for (Reservation reservation : candidate.getReservations()) {
            Room room = candidate.getRoomByReservation(reservation);
            for (Requests request : Requests.values()) {
                if (!room.doesComplyWithRequest(request)) {
                    RequestImportance importance = reservation.getImportance(request);
                    switch (importance) {
                        case NOT_IMPORTANT:
                            break;
                        case NICE_TO_HAVE:
                            fitness -= 2; // TODO: change score according to the maximun fitness
                            break;
                        case MUST:
                            fitness -= 4; // TODO: change score according to the maximun fitness
                            break;
                    }
                }
            }
        }
        return fitness;
    }

    public boolean isNatural() {
        return false;
    }
}