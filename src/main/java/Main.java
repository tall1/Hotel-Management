import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.operators.StringCrossover;
import org.uncommons.watchmaker.framework.operators.StringMutation;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Main {
/*    public static void main(String[] args) {
        List<Room> rooms = new ArrayList<>();
        List<Reservation> reservations = new ArrayList<>();
        init(rooms, reservations);
        // Get the singleton instance of lobby:
        Lobby lobby = Lobby.getInstance((ArrayList<Room>) rooms, (ArrayList<Reservation>) reservations);
        System.out.println(lobby);
        Assignment assignment = new Assignment(lobby);
        System.out.println(assignment);
    }*/
    public static void main(String[] args) {
        // Create a factory to generate random 11-character Strings.
/*
        char[] chars = new char[27];
        for (char c = 'A'; c <= 'Z'; c++) {
            chars[c - 'A'] = c;
        }
        chars[26] = ' ';
*/


        List<Room> rooms = new ArrayList<>();
        List<Reservation> reservations = new ArrayList<>();
        init(rooms, reservations);
        // Get the singleton instance of lobby:
        Lobby lobby = Lobby.getInstance((ArrayList<Room>) rooms, (ArrayList<Reservation>) reservations);

//        CandidateFactory<String> factory = new StringFactory(chars, 11);
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
        rooms.add(new Room());
        rooms.add(new Room());
        rooms.add(new Room());
        rooms.add(new Room());

        reservations.add(new Reservation(3));
        reservations.add(new Reservation(4));
    }
}
