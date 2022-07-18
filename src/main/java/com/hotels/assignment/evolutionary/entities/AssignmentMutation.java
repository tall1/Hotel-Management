package com.hotels.assignment.evolutionary.entities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.hotels.assignment.Assignment;
import com.hotels.assignment.entities.Lobby;
import com.hotels.assignment.entities.Reservation;
import com.hotels.assignment.entities.Room;
import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

public class AssignmentMutation implements EvolutionaryOperator<Assignment> {

    private final List<Room> rooms;
    private final List<Reservation> reservations;
    private final NumberGenerator<Probability> mutationProbability;

    public AssignmentMutation(Lobby lobby, Probability mutationProbability) {
        this(lobby, new ConstantGenerator(mutationProbability));
    }

    public AssignmentMutation(Lobby lobby, NumberGenerator<Probability> mutationProbability) {
        this.rooms = lobby.getRoomArrayList();
        this.reservations = lobby.getReservationArrayList();
        this.mutationProbability = mutationProbability;
    }

    public List<Assignment> apply(List<Assignment> selectedCandidates, Random rng) {
        List<Assignment> mutatedPopulation = new ArrayList<>(selectedCandidates.size());
        Iterator i$ = selectedCandidates.iterator();

        while (i$.hasNext()) {
            Assignment assignment = (Assignment) i$.next();
            mutatedPopulation.add(this.mutateAssignment(assignment, rng));
        }

        return mutatedPopulation;
    }

    public Assignment mutateAssignment(Assignment oldAssignment, Random rng) {
        Assignment newAssignment = new Assignment(oldAssignment);
        for (int i = 0; i < newAssignment.getAmountOfReservations(); ++i) {
            if (((Probability) this.mutationProbability.nextValue()).nextEvent(rng)) {
                Reservation currentReservation = reservations.get(i);
                Room randomRoom = this.rooms.get(rng.nextInt(this.rooms.size() - 1));

                newAssignment.assign(randomRoom, currentReservation);
            }
        }

        return newAssignment;
    }
}
