import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssignmentEvaluator implements FitnessEvaluator<Assignment> {
    private static AssignmentEvaluator assignmentEvaluatorInstance = null;
    private final List<Double> score_levels;
    private final int amount_of_score_levels = 5;


    private AssignmentEvaluator(double level_1_score) {
        this.score_levels = new ArrayList<>(this.amount_of_score_levels);
        double current_score = level_1_score;
        for (int i = 0; i < amount_of_score_levels; ++i) {
            score_levels.add(i, current_score);
            current_score /= 2;
        }
    }

    public static AssignmentEvaluator getInstance() {
        return assignmentEvaluatorInstance;
    }

    public static AssignmentEvaluator getInstance(double level_1_score) {
        if (assignmentEvaluatorInstance == null) {
            assignmentEvaluatorInstance = new AssignmentEvaluator(level_1_score);
        }
        return assignmentEvaluatorInstance;
    }

    /**
     * Fines the fitness for every:
     * 1. Room with multiple reservations.
     * 2. Insufficient room.
     * 3. Unfulfilled request, according to the importance.
     */
    public double getFitness(Assignment candidate
            , List<? extends Assignment> population) {

        double fitness = 0;
        // Handle multiple reservations per room:
        Map<Room, Integer> amountOfReservationsPerRoom = candidate.getAmountOfReservationsPerRoom(); // Includes reserved and unreserved rooms
        for (Integer amountOfReservations : amountOfReservationsPerRoom.values()) {
            if (amountOfReservations <= 1) {
                fitness += this.score_levels.get(0);
            }
        }

        // Handle more guests than room capacity:
        int amountOfSufficientRooms = candidate.getAmountOfSufficientRooms(); // Includes only reserved rooms
        fitness += amountOfSufficientRooms * this.score_levels.get(1);

        // Handle fulfilled requests:
        for (Reservation reservation : candidate.getReservations()) {
            Room room = candidate.getRoomByReservation(reservation);
            for (Request request : Request.values()) {
                if (room.doesComplyWithRequest(request)) {
                    fitness = fitnessEvalHelper(fitness, reservation, request);
                }
            }
        }
        return fitness;
    }

    public double getMaxFitness(Assignment candidate) {
        double maxFitness = 0;
        maxFitness += candidate.getAmountOfRooms() * this.score_levels.get(0);
        maxFitness += candidate.getAmountOfReservations() * this.score_levels.get(1);
        for (Reservation reservation : candidate.getReservations()) {
            for (Request request : Request.values()) {
                maxFitness = fitnessEvalHelper(maxFitness, reservation, request);
            }
        }
        return maxFitness;
    }

    private double fitnessEvalHelper(double fitness, Reservation reservation, Request request) {
        RequestImportance importance = reservation.getImportance(request);
        switch (importance) {
            case MUST:
                fitness += this.score_levels.get(2);
                break;
            case NICE_TO_HAVE:
                fitness += this.score_levels.get(3);
                break;
            case NOT_IMPORTANT:
                fitness += this.score_levels.get(4);
                break;
        }
        return fitness;
    }

    public boolean isNatural() {
        return true;
    }
}