package com.hotels.entities.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotels.entities.feature.Feature;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoomDTO {
    private int id;
    private int roomNumber;
    private int hotelId;
    private int floorNumber;
    private int roomCapacity;
    private String availableDate;
    private List<Integer> featureIdsList = new ArrayList<>();

    @JsonIgnore
    public void setFeatureIdsListByFeatureList(List<Feature> featureList) {
        featureList.forEach(feature -> this.featureIdsList.add(feature.getId()));
    }
}
