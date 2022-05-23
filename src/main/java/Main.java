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

        // Create a pipeline that applies cross-over then mutation.
        List<EvolutionaryOperator<Assignment>> operators = new LinkedList<>();
        operators.add(new AssignmentMutation(lobby, new Probability(0.2)));
        operators.add(new AssignmentCrossover());
        EvolutionaryOperator<Assignment> pipeline
                = new EvolutionPipeline<>(operators);

        AssignmentEvaluator fitnessEvaluator = AssignmentEvaluator.getInstance();
        SelectionStrategy<Object> selection = new RouletteWheelSelection();
        Random rng = new MersenneTwisterRNG();

        EvolutionEngine<Assignment> engine
                = new GenerationalEvolutionEngine<>(factory,
                pipeline,
                fitnessEvaluator,
                selection,
                rng);

        int maxFitness = fitnessEvaluator.getMaxFitness(lobby.getAmountOfReservations());

        engine.addEvolutionObserver(data -> System.out.printf("Generation %d: Best candidate fitness: %s / %d\n",
                data.getGenerationNumber(),
                fitnessEvaluator.getFitness(data.getBestCandidate(), null),
                maxFitness));


        Assignment result = engine.evolve(50, 5, new TargetFitness(maxFitness, true));
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
