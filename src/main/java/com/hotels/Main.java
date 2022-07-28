package com.hotels;

import com.hotels.assignment.*;
import com.hotels.entities.Lobby;
import com.hotels.entities.roomreservationfeature.Reservation;
import com.hotels.entities.roomreservationfeature.Room;
import com.hotels.assignment.evolutionary.entities.AssignmentCrossover;
import com.hotels.assignment.evolutionary.entities.AssignmentEvaluator;
import com.hotels.assignment.evolutionary.entities.AssignmentFactory;
import com.hotels.assignment.evolutionary.entities.AssignmentMutation;
import com.hotels.service.utils.EngineProperties;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Main {
    private final EvolutionEngine<Assignment> engine;

    private final EngineProperties ep;

    private Lobby lobby;

    private final AssignmentEvaluator fitnessEvaluator;

    private final Double maxFitness;
    private final static int numOfRooms = 20;
    private final static int numOfReservations = 12;

    public static void main(String[] args) {
        Main m1 = new Main(new EngineProperties());
        Assignment assignment = m1.getAssignment();
        System.out.println(assignment);
    }

    public Main(EngineProperties ep) {
        this.ep = ep;
        getLobby();
        CandidateFactory<Assignment> factory = new AssignmentFactory(lobby);
        EvolutionaryOperator<Assignment> pipeline = getPipeline(ep);
        fitnessEvaluator = new AssignmentEvaluator();
        SelectionStrategy<Object> selection = ep.getSelectionStrategy();
        Random rng = new MersenneTwisterRNG();

        this.engine = new GenerationalEvolutionEngine<>(
                factory,
                pipeline,
                this.fitnessEvaluator,
                selection,
                rng);

        this.maxFitness = fitnessEvaluator.getMaxFitness(
                lobby.getAmountOfReservations() // TODO: I don't know about this. looks cumbersome.
        );
    }

    private void getLobby() {
        ArrayList<Room> rooms = new ArrayList<>(); // TODO: Has 2 b ArrayList @ declaration?
        ArrayList<Reservation> reservations = new ArrayList<>(); // TODO: Has 2 b ArrayList @ declaration?

        init(rooms, reservations); // TODO: Need to change this to retrieve data from DB.
        this.lobby = new Lobby(rooms, reservations);
    }

    private EvolutionaryOperator<Assignment> getPipeline(EngineProperties ep) {
        // Create a pipeline that applies cross-over then mutation.
        List<EvolutionaryOperator<Assignment>> operators = new LinkedList<>();
        operators.add(new AssignmentMutation(lobby, ep.getMutationProb()));
        operators.add(new AssignmentCrossover());
        return new EvolutionPipeline<>(operators);
    }

    public Assignment getAssignment() {
        engine.addEvolutionObserver(data ->
                System.out.printf("Generation %d: Best candidate fitness: %s / %f\n",
                data.getGenerationNumber(),
                fitnessEvaluator.getFitness(data.getBestCandidate(), null),
                maxFitness));

        TerminationCondition[] termCondArr = new TerminationCondition[ep.getTermCond().size()];
        // i = population size, i1 = elitism:
        return engine.evolve(50, 5, termCondArr);
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
