package com.hotels.entities.reservation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotels.entities.roomreservationfeature.ReservationFeature;
import com.hotels.utils.FeatureCounter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Setter
public class ReservationDTO {
    private Integer reservationNumber;
    private int hotelId;
    private String guestName;
    private int guestsAmount;
    private String checkin;
    private String checkout;
    private List<Integer> importanceList = new ArrayList<>((int) (FeatureCounter.getAmountOfFeatures() + 1) ); // index = feature id, value = importance

    @JsonIgnore
    public void setImportanceListByReservationFeatureList(List<ReservationFeature> reservationFeatures) {
        // Initialize:
        for (int i = 0; i <= reservationFeatures.size(); i++) {
            this.importanceList.add(-1);
        }
        // Set reservationFeatureIds: index = featureId, value = importance
        for (ReservationFeature reservationFeature : reservationFeatures) {
            this.importanceList.set(reservationFeature.getFeature().getId(), reservationFeature.getImportance());
        }
    }
}
