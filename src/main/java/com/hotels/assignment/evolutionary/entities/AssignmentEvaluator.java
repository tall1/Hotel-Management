package com.hotels.assignment.evolutionary.entities;

import com.hotels.assignment.Assignment;
import com.hotels.entities.enums.RequestImportance;
import com.hotels.entities.roomreservationfeature.Feature;
import com.hotels.entities.roomreservationfeature.Reservation;
import com.hotels.entities.roomreservationfeature.ReservationFeature;
import com.hotels.entities.roomreservationfeature.Room;
import lombok.NoArgsConstructor;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class AssignmentEvaluator implements FitnessEvaluator<Assignment> {
    final int maxFitnessPerReservation = 20;
    final int damageForMultipleReservations = 5; // 5
    final int damageForCapacity = 3; // 3
    final int damageForRequest = 2; // == 12

    /**
     * Finds the fitness for every:
     * 1. Room with multiple reservations.
     * 2. Insufficient room.
     * 3. Unfulfilled request, according to the importance.
     */
    public double getFitness(Assignment candidate
            , List<? extends Assignment> population) {
        double fitness = candidate.getAmountOfReservations() * maxFitnessPerReservation;

        Map<Room, Integer> amountOfReservationsPerRoom = candidate.getAmountOfReservationsPerRoom(); // Includes reserved and unreserved rooms
        // MAIN LOOP:
        for (Reservation reservation : candidate.getReservations()) {
            // Handle multiple reservations per room:
            Room reservedRoom = candidate.getRoomByReservation(reservation);
            if (amountOfReservationsPerRoom.get(reservedRoom) > 1) {
                fitness -= damageForMultipleReservations;
            }
            // Handle more guests than room capacity:
            if (reservation.getGuestsAmount() > reservedRoom.getRoomCapacity()) {
                fitness -= damageForCapacity;
            }
            // Handle unfulfilled requests:
            synchronized (this) {
                for (ReservationFeature reservationFeature : reservation.getReservationFeatures()) {
                    Feature feature = reservationFeature.getFeature();
                    if (feature != null && !reservedRoom.doesHaveFeature(feature)) {
                        fitness = fitnessEvalHelper(fitness, reservationFeature.getImportance());
                    }
                }
            }
        }
        return fitness;
    }

    private double fitnessEvalHelper(double fitness, Integer importance) {
        switch (RequestImportance.getRequestImportanceByInt(importance)) {
            case MUST:
                fitness -= damageForRequest;
                break;
            case NICE_TO_HAVE:
                fitness -= (damageForRequest - 1);
                break;
            default:
                break;
        }
        return fitness;
    }

    public Double getMaxFitness(int amountOfReservations) {
        return (double) (this.maxFitnessPerReservation * amountOfReservations);
    }

    public boolean isNatural() {
        return true;
    }
}