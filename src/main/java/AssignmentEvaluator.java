import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.List;
import java.util.Map;

public class AssignmentEvaluator implements FitnessEvaluator<Assignment> {
    private static AssignmentEvaluator assignmentEvaluatorInstance = null;

    final int maxFitnessPerReservation = 20;
    final int damageForMultipleReservations = 5; // 5
    final int damageForCapacity = 3; // 3
    final int damageForRequest = 2; // == 12

    private AssignmentEvaluator() {}


    public static AssignmentEvaluator getInstance() {
        if (assignmentEvaluatorInstance == null) {
            assignmentEvaluatorInstance = new AssignmentEvaluator();
        }
        return assignmentEvaluatorInstance;
    }

    /**
     * Finds the fitness for every:
     * 1. Room with multiple reservations.
     * 2. Insufficient room.
     * 3. Unfulfilled request, according to the importance.
     */
    public double getFitness(Assignment candidate
            , List<? extends Assignment> population) {
        double fitness = candidate.getAmountOfReservations() * maxFitnessPerReservation;

        Map<Room, Integer> amountOfReservationsPerRoom = candidate.getAmountOfReservationsPerRoom(); // Includes reserved and unreserved rooms
        // MAIN LOOP:
        for (Reservation reservation : candidate.getReservations()) {
            // Handle multiple reservations per room:
            Room reservedRoom = candidate.getRoomByReservation(reservation);
            if (amountOfReservationsPerRoom.get(reservedRoom) > 1) {
                fitness -= damageForMultipleReservations;
            }
            // Handle more guests than room capacity:
            if (reservation.getGuestsAmount() > reservedRoom.getRoomCapacity()) {
                fitness -= damageForCapacity;
            }
            // Handle unfulfilled requests:
            for (Request request : Request.values()) {
                if (!reservedRoom.doesComplyWithRequest(request)) {
                    fitness = fitnessEvalHelper(fitness, reservation, request);
                }
            }
        }
        return fitness;
    }

    private double fitnessEvalHelper(double fitness, Reservation reservation, Request request) {
        RequestImportance importance = reservation.getImportance(request);
        switch (importance) {
            case MUST:
                fitness -= damageForRequest;
                break;
            case NICE_TO_HAVE:
                fitness -= (damageForRequest - 1);
                break;
            default:
                break;
        }
        return fitness;
    }

    public int getMaxFitness(int amountOfReservations){
        return this.maxFitnessPerReservation * amountOfReservations;
    }

    public boolean isNatural() {
        return true;
    }
}