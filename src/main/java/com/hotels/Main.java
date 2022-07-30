package com.hotels;

import com.hotels.assignment.Assignment;
import com.hotels.assignment.evolutionary.entities.AssignmentCrossover;
import com.hotels.assignment.evolutionary.entities.AssignmentEvaluator;
import com.hotels.assignment.evolutionary.entities.AssignmentFactory;
import com.hotels.assignment.evolutionary.entities.AssignmentMutation;
import com.hotels.entities.Lobby;
import com.hotels.entities.roomreservationfeature.Reservation;
import com.hotels.entities.roomreservationfeature.Room;
import com.hotels.repository.ReservationRepository;
import com.hotels.repository.RoomRepository;
import com.hotels.service.utils.EngineProperties;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@NoArgsConstructor
public class Main {
    @Autowired
    ReservationRepository resRepos;
    @Autowired
    RoomRepository roomRepos;

    @PostConstruct
    public void postConstructor() {
        // here put any after construction operations
        System.out.println("Main: @PostConstruct");
    }

    private final static int numOfRooms = 20;
    private final static int numOfReservations = 12;

    public static void main(String[] args) {
        Main m1 = new Main();
        Assignment assignment = m1.getAssignment(new EngineProperties());
        System.out.println(assignment);
    }

    public Assignment getAssignment(EngineProperties ep) {
        Lobby lobby = getLobby();
        CandidateFactory<Assignment> factory = new AssignmentFactory(lobby);
        EvolutionaryOperator<Assignment> pipeline = getPipeline(ep, lobby);
        AssignmentEvaluator fitnessEvaluator = new AssignmentEvaluator();
        SelectionStrategy<Object> selection = ep.getSelectionStrategy();
        Random rng = new MersenneTwisterRNG();

        EvolutionEngine<Assignment> engine = new GenerationalEvolutionEngine<>(
                factory,
                pipeline,
                fitnessEvaluator,
                selection,
                rng);

        double maxFitness = fitnessEvaluator.getMaxFitness(
                lobby.getAmountOfReservations() // TODO: I don't know about this. looks cumbersome.
        );

        return runEngine(engine, fitnessEvaluator, maxFitness, ep);
    }

    private Lobby getLobby() {
        List<Room> rooms = roomRepos.findRoomsByHotelId(1);
        List<Reservation> reservations = resRepos.findReservationsByHotelId(1);
        //init(rooms, reservations); // TODO: Need to change this to retrieve data from DB.

        return new Lobby(rooms, reservations);
    }

    private EvolutionaryOperator<Assignment> getPipeline(EngineProperties ep, Lobby lobby) {
        // Create a pipeline that applies cross-over then mutation.
        List<EvolutionaryOperator<Assignment>> operators = new LinkedList<>();
        operators.add(new AssignmentMutation(lobby, ep.getMutationProb()));
        operators.add(new AssignmentCrossover());
        return new EvolutionPipeline<>(operators);
    }

    public Assignment runEngine(
            EvolutionEngine<Assignment> engine,
            AssignmentEvaluator fitnessEvaluator,
            Double maxFitness,
            EngineProperties ep) {
        engine.addEvolutionObserver(data ->
                System.out.printf("Generation %d: Best candidate fitness: %s / %f\n",
                        data.getGenerationNumber(),
                        fitnessEvaluator.getFitness(data.getBestCandidate(), null),
                        maxFitness));

        TerminationCondition[] termCondArr = ep.getTermCond().toArray(new TerminationCondition[0]);
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
