package com.hotels.assignment.evolutionary.entities;

import com.hotels.assignment.Assignment;
import com.hotels.entities.Lobby;
import com.hotels.entities.roomreservationfeature.Reservation;
import com.hotels.entities.roomreservationfeature.Room;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import java.util.*;

public class AssignmentFactory extends AbstractCandidateFactory<Assignment> {

    private Lobby lobby;

    public AssignmentFactory(Lobby lobby) {
        this.lobby = lobby;
    }

    public Assignment generateRandomCandidate(Random rng) {
        Map<Reservation, Room> assignmentSuggestion = new HashMap<>();
        Room room;
        // Assign random room for each reservation:
        for (Reservation reservation : lobby.getReservationArrayList()) {
            room = lobby.getRandomRoom();
            assignmentSuggestion.put(reservation, room);
            // Enable multiple reservations per room:
            if(room.getIsAvailable()){
                room.setIsAvailable(false);
                lobby.addOrRemoveRoomFromAvailable(room);
            }
        }
        return new Assignment(lobby, assignmentSuggestion);
    }
}
