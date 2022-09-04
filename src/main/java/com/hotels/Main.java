package com.hotels;

import com.hotels.assignment.evolutionary.entities.AssignmentCrossover;
import com.hotels.assignment.evolutionary.entities.AssignmentEvaluator;
import com.hotels.assignment.evolutionary.entities.AssignmentFactory;
import com.hotels.assignment.evolutionary.entities.AssignmentMutation;
import com.hotels.entities.assignment.Assignment;
import com.hotels.entities.feature.Feature;
import com.hotels.entities.hotel.Hotel;
import com.hotels.entities.lobby.Lobby;
import com.hotels.entities.reservation.Reservation;
import com.hotels.entities.room.Room;
import com.hotels.entities.roomreservationfeature.ReservationFeature;
import com.hotels.entities.task.Task;
import com.hotels.entities.task.TaskProperties;
import com.hotels.entities.task.status.TaskStatus;
import com.hotels.exceptions.EmptyReservationListException;
import com.hotels.exceptions.EmptyRoomListException;
import com.hotels.repository.TaskRepository;
import com.hotels.repository.TaskStatusRepository;
import com.hotels.utils.MyConstants;
import org.springframework.stereotype.Component;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.*;

@Component
public class Main {

    private final static int numOfRooms = 30;
    private final static int numOfReservations = 20;


    public static void main(String[] args) {
        List<Room> roomList = new ArrayList<>();
        List<Reservation> resList = new ArrayList<>();
        init(roomList, resList);
        Assignment assignment = Main.getAssignment(MyConstants.EMPTY_TASK_ID, new TaskProperties(), roomList, resList);
        System.out.println(assignment);
    }

    public static Assignment getAssignment(long taskId, TaskProperties taskProperties, List<Room> roomList, List<Reservation> resList) {
        if (roomList.size() == 0) {
            throw new EmptyRoomListException();
        }
        if (resList.size() == 0) {
            throw new EmptyReservationListException();
        }
        Lobby lobby = new Lobby(roomList, resList);
        CandidateFactory<Assignment> factory = new AssignmentFactory(lobby);
        EvolutionaryOperator<Assignment> pipeline = getPipeline(taskProperties, lobby);
        AssignmentEvaluator fitnessEvaluator = new AssignmentEvaluator();
        SelectionStrategy<Object> selection = taskProperties.getSelectionStrategy();
        Random rng = new MersenneTwisterRNG();

        EvolutionEngine<Assignment> engine = new GenerationalEvolutionEngine<>(
                factory,
                pipeline,
                fitnessEvaluator,
                selection,
                rng);

        double maxFitness = fitnessEvaluator.getMaxFitness(
                lobby.getAmountOfReservations()
        );

        return runEngine(taskId, engine, fitnessEvaluator, maxFitness, taskProperties);
    }

    private static EvolutionaryOperator<Assignment> getPipeline(TaskProperties taskProperties, Lobby lobby) {
        // Create a pipeline that applies cross-over then mutation.
        List<EvolutionaryOperator<Assignment>> operators = new LinkedList<>();
        operators.add(new AssignmentMutation(lobby, taskProperties.getMutationProb()));
        operators.add(new AssignmentCrossover());
        return new EvolutionPipeline<>(operators);
    }

    public static Assignment runEngine(long taskId, EvolutionEngine<Assignment> engine, AssignmentEvaluator fitnessEvaluator, Double maxFitness, TaskProperties taskProperties) {
        addEvolutionObservers(taskId, engine, fitnessEvaluator, maxFitness);
        TerminationCondition[] termCondArr = taskProperties.getTermCond().toArray(new TerminationCondition[0]);
        // i = population size, i1 = elitism:
        return engine.evolve(50, 5, termCondArr);
    }

    private static void addEvolutionObservers(long taskId, EvolutionEngine<Assignment> engine, AssignmentEvaluator fitnessEvaluator, Double maxFitness) {
        /*engine.addEvolutionObserver(data ->
                System.out.printf("Generation %d: Best candidate fitness: %s / %f\n",
                        data.getGenerationNumber(),
                        fitnessEvaluator.getFitness(data.getBestCandidate(), null),
                        maxFitness));*/
        if (taskId != MyConstants.EMPTY_TASK_ID) {
            engine.addEvolutionObserver(data ->
            {
                Optional<Task> taskOpt = MainAccessor.getBean(TaskRepository.class).findById(taskId);
                taskOpt.orElseThrow(() -> new EntityNotFoundException("Task " + taskId + " status not found for adding evolution observer."));

                TaskStatus taskStatus = taskOpt.get().getStatus();
                taskStatus.setBestFitness(data.getBestCandidateFitness());
                taskStatus.setCurGeneration(data.getGenerationNumber());
                taskStatus.setElapsedTime(data.getElapsedTime());

                MainAccessor.getBean(TaskStatusRepository.class).save(taskStatus);
            });
        }
    }

    private static void init(List<Room> rooms, List<Reservation> reservations) {
        String[] featureNames = {"elevator proximity", "sea view", "bathtub", "balcony", "handicapped", "high floor"};
        Hotel hotel = new Hotel();
        hotel.setId(1);
        hotel.setHotelName("Tal's hotel");
        List<Feature> featureList = new ArrayList<>();
        for (int i = 0; i < Main.numOfRooms; ++i) {
            featureList.clear();
            for (int j = 1; j < 6; j++) {
                Feature feature = new Feature();
                feature.setId(j);
                feature.setFeatureName(featureNames[j]);
                if (Math.random() > 0.5) {
                    featureList.add(feature);
                }
            }
            rooms.add(new Room(i, i, hotel, 4, featureList));
        }

        List<ReservationFeature> reservationFeatures = new ArrayList<>();
        for (int i = 0; i < Main.numOfReservations; ++i) {
            reservationFeatures.clear();
            for (int j = 1; j < 6; j++) {
                Feature feature = new Feature();
                feature.setId(j);
                feature.setFeatureName(featureNames[j]);

                Reservation fakeReservation = new Reservation();
                fakeReservation.setReservationNumber(i);

                Integer importance = Math.random() < 0.33 ? 3 : Math.random() < 0.5 ? 2 : 1;

                ReservationFeature reservationFeature = new ReservationFeature();
                reservationFeature.setReservation(fakeReservation);
                reservationFeature.setFeature(feature);
                reservationFeature.setImportance(importance);

                reservationFeatures.add(reservationFeature);
            }
            LocalDate checkin = LocalDate.now();
            LocalDate checkout = checkin.plusDays(2);
            reservations.add(new Reservation(i, hotel, "Adi", 3, checkin, checkout, reservationFeatures));
        }
    }
}
