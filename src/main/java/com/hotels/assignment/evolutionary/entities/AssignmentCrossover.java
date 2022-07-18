package com.hotels.assignment.evolutionary.entities;

import com.hotels.assignment.Assignment;
import com.hotels.assignment.entities.Reservation;
import com.hotels.assignment.entities.Room;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;
import java.util.*;

public class AssignmentCrossover extends AbstractCrossover<Assignment> {


    public AssignmentCrossover() {
        this(1);
    }

    public AssignmentCrossover(int crossoverPoints) {
        super(crossoverPoints);
    }

    /**
     * Receives two Assignments and crossovers one another.
     * Assumptions:
     * 1. The two assignments are on the same hotel (same com.hotels.assignment.entities.Lobby)
     * 2. The two assignments are on the same Reservations list (reservation in a1 <=> reservation in a2)
     */
    protected List<Assignment> mate(Assignment a1, Assignment a2, int numberOfCrossoverPoints, Random rng) {
        if (a1.getLobbyHashcode() != a2.getLobbyHashcode()) {
            throw new IllegalArgumentException("Cannot perform cross-over with different hotels.");
        }

        Assignment offspring1 = new Assignment(a1);
        Assignment offspring2 = new Assignment(a2);
        List<Assignment> result = new ArrayList<>(2);

        final int amountOfReservations = a1.getAmountOfReservations();
        final Collection<Reservation> reservations = a1.getReservations();

        // TODO: Break these steps into methods


        // Step 1: Map every reservation to a List's index:
        HashMap<Reservation, Integer> reservationToIndexMap = new HashMap<>(amountOfReservations);
        HashMap<Integer, Reservation> indexToReservationMap = new HashMap<>(amountOfReservations);
        final int[] idx = {0};
        reservations.forEach(reservation -> {
            reservationToIndexMap.put(reservation, idx[0]);
            indexToReservationMap.put(idx[0], reservation);
            idx[0]++;
        });

        // Step 2: Map every index to a room according to a1\a2:
        List<Room> roomList1 = new ArrayList<>(amountOfReservations);
        List<Room> roomList2 = new ArrayList<>(amountOfReservations);
        int index;
        Room room1, room2;
        for (Reservation reservation : reservations) {
            index = reservationToIndexMap.get(reservation);
            room1 = offspring1.getRoomByReservation(reservation);
            room2 = offspring2.getRoomByReservation(reservation);
            roomList1.add(index, room1);
            roomList2.add(index, room2);
        }

        // Step 3: Crossover the lists:
        for (int i = 0; i < numberOfCrossoverPoints; ++i) {
            int max = Math.min(roomList1.size(), roomList2.size());
            if (max > 1) {
                int crossoverIndex = 1 + rng.nextInt(max - 1);

                for (int j = 0; j < crossoverIndex; ++j) {
                    Room temp = roomList1.get(j);
                    roomList1.set(j, roomList2.get(j));
                    roomList2.set(j, temp);
                }
            }
        }

        // Step 4: Insert the lists to the offspring assignments
        Reservation reservation;
        for (int i = 0; i < amountOfReservations; i++) {
            reservation = indexToReservationMap.get(i);
            room1 = roomList1.get(i);
            room2 = roomList2.get(i);
            offspring1.assign(room1, reservation);
            offspring2.assign(room2, reservation);
        }

        result.add(offspring1);
        result.add(offspring2);
        return result;
    }
}
