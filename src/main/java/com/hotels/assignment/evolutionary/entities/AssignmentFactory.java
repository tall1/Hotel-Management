package com.hotels.assignment.evolutionary.entities;

import com.hotels.entities.assignment.Assignment;
import com.hotels.entities.lobby.Lobby;
import com.hotels.entities.reservation.Reservation;
import com.hotels.entities.room.Room;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import java.util.*;

public class AssignmentFactory extends AbstractCandidateFactory<Assignment> {

    private final Lobby lobby;

    public AssignmentFactory(Lobby lobby) {
        this.lobby = lobby;
    }

    public Assignment generateRandomCandidate(Random rng) {
        Map<Reservation, Room> assignmentSuggestion = new HashMap<>();
        Room room;
        // Assign random room for each reservation:
        for (Reservation reservation : lobby.getReservationList()) {
            room = lobby.getRandomRoom();
            assignmentSuggestion.put(reservation, room);
        }
        return new Assignment(lobby, assignmentSuggestion);
    }
}
