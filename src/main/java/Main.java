import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Main {
    private final static int numOfRooms = 20;
    private final static int numOfReservations = 12;

    public static void main(String[] args) {
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
        AssignmentCrossover assignmentCrossover = new AssignmentCrossover();
        System.out.println("Assignment 0: " + assignments.get(0));
        System.out.println("Assignment 1: " + assignments.get(1));
        List<Assignment> offsprings = assignmentCrossover.mate(assignments.get(0), assignments.get(1), 4, rng);
        System.out.println("Offspring 0: " + offsprings.get(0));
        System.out.println("Offspring 1: " + offsprings.get(1));
    }

    public static void main1(String[] args) {
        ArrayList<Room> rooms = new ArrayList<>();
        ArrayList<Reservation> reservations = new ArrayList<>();
        init(rooms, reservations);
        // Get the singleton instance of lobby:
        Lobby lobby = Lobby.getInstance(rooms, reservations);

        CandidateFactory<Assignment> factory = new AssignmentFactory(lobby);

        // Create a pipeline that applies cross-over then mutation.
        List<EvolutionaryOperator<Assignment>> operators = new LinkedList<>();
        operators.add(new AssignmentMutation(rooms, reservations, new Probability(0.02)));
        operators.add(new AssignmentCrossover());
        EvolutionaryOperator<Assignment> pipeline
                = new EvolutionPipeline<>(operators);

        FitnessEvaluator<Assignment> fitnessEvaluator = new AssignmentEvaluator();
        SelectionStrategy<Object> selection = new RouletteWheelSelection();
        Random rng = new MersenneTwisterRNG();

        EvolutionEngine<Assignment> engine
                = new GenerationalEvolutionEngine<>(factory,
                pipeline,
                fitnessEvaluator,
                selection,
                rng);

        engine.addEvolutionObserver(data -> System.out.printf("Generation %d: %s\n",
                data.getGenerationNumber(),
                data.getBestCandidate()));

        Assignment result = engine.evolve(10, 1, new TargetFitness(11, true));
        System.out.println(result);
    }

    private static void init(List<Room> rooms, List<Reservation> reservations) {
        for (int i = 0; i < Main.numOfRooms; ++i) {
            rooms.add(new Room());
        }
        for (int i = 0; i < Main.numOfReservations; ++i) {
            reservations.add(new Reservation());
        }
    }
}
