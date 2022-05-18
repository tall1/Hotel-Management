import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Main {
    private final static int numOfRooms = 20;
    private final static int numOfReservations = 12;

    public static void main1(String[] args) {
        ArrayList<Room> rooms = new ArrayList<>();
        ArrayList<Reservation> reservations = new ArrayList<>();
        init(rooms, reservations);
        // Get the singleton instance of lobby:
        Lobby lobby = Lobby.getInstance(rooms, reservations);
        CandidateFactory<Assignment> factory = new AssignmentFactory(lobby);
        Random rng = new MersenneTwisterRNG();
        final int num = 5;
        List<Assignment> assignments = new ArrayList<>(num);
        for (int i = 0; i < num; ++i) {
            assignments.add(factory.generateRandomCandidate(rng));
        }
        AssignmentEvaluator assignmentEvaluator = AssignmentEvaluator.getInstance(100);
        AssignmentMutation assignmentMutation = new AssignmentMutation(lobby, new Probability(0.5));
        List<Assignment> mutated = assignmentMutation.apply(assignments, rng);
        int i = 0;
        for (Assignment assignment : mutated) {
            System.out.println("Assignment " + (i++) + ": ");
            System.out.println("\tMax fitness = " + assignmentEvaluator.getMaxFitness(assignment));
            System.out.println("\tFitness = " + assignmentEvaluator.getFitness(assignment, null));
        }

    }

    public static void main(String[] args) {
        ArrayList<Room> rooms = new ArrayList<>();
        ArrayList<Reservation> reservations = new ArrayList<>();
        init(rooms, reservations);
        // Get the singleton instance of lobby:
        Lobby lobby = Lobby.getInstance(rooms, reservations);

        CandidateFactory<Assignment> factory = new AssignmentFactory(lobby);

        // Create a pipeline that applies cross-over then mutation.
        List<EvolutionaryOperator<Assignment>> operators = new LinkedList<>();
        operators.add(new AssignmentMutation(lobby, new Probability(0.2)));
        operators.add(new AssignmentCrossover());
        EvolutionaryOperator<Assignment> pipeline
                = new EvolutionPipeline<>(operators);

        AssignmentEvaluator fitnessEvaluator = AssignmentEvaluator.getInstance(100.0);
        SelectionStrategy<Object> selection = new RouletteWheelSelection();
        Random rng = new MersenneTwisterRNG();

        EvolutionEngine<Assignment> engine
                = new GenerationalEvolutionEngine<>(factory,
                pipeline,
                fitnessEvaluator,
                selection,
                rng);

        engine.addEvolutionObserver(data -> System.out.printf("Generation %d: %s / %s\n",
                data.getGenerationNumber(),
                fitnessEvaluator.getMaxFitness(data.getBestCandidate()),
                fitnessEvaluator.getFitness(data.getBestCandidate(), null)));

        Assignment result = engine.evolve(50, 3, new TargetFitness(3600, true));
        System.out.println(result);
    }

    private static void init(List<Room> rooms, List<Reservation> reservations) {
        for (int i = 0; i < Main.numOfRooms; ++i) {
            rooms.add(new Room(10));
        }
        for (int i = 0; i < Main.numOfReservations; ++i) {
            reservations.add(new Reservation((int) (Math.random() * 10)));
        }
    }
}
