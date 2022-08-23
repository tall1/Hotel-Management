package com.hotels.assignment.evolutionary.entities;

import com.hotels.entities.assignment.Assignment;
import com.hotels.entities.lobby.Lobby;
import com.hotels.entities.reservation.Reservation;
import com.hotels.entities.room.Room;
import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AssignmentMutation implements EvolutionaryOperator<Assignment> {

    private final List<Room> rooms;
    private final List<Reservation> reservations;
    private final NumberGenerator<Probability> mutationProbability;

    public AssignmentMutation(Lobby lobby, Probability mutationProbability) {
        this(lobby, new ConstantGenerator<>(mutationProbability));
    }

    public AssignmentMutation(Lobby lobby, NumberGenerator<Probability> mutationProbability) {
        this.rooms = lobby.getRoomList();
        this.reservations = lobby.getReservationList();
        this.mutationProbability = mutationProbability;
    }

    public List<Assignment> apply(List<Assignment> selectedCandidates, Random rng) {
        List<Assignment> mutatedPopulation = new ArrayList<>(selectedCandidates.size());

        for (Assignment assignment : selectedCandidates) {
            mutatedPopulation.add(this.mutateAssignment(assignment, rng));
        }

        return mutatedPopulation;
    }

    public Assignment mutateAssignment(Assignment oldAssignment, Random rng) {
        Assignment newAssignment = new Assignment(oldAssignment);
        for (int i = 0; i < newAssignment.getAmountOfReservations(); ++i) {
            if ((this.mutationProbability.nextValue()).nextEvent(rng)) {
                Reservation currentReservation = reservations.get(i);
                Room randomRoom = this.rooms.get(rng.nextInt(this.rooms.size() - 1));

                newAssignment.assign(randomRoom, currentReservation);
            }
        }

        return newAssignment;
    }
}
