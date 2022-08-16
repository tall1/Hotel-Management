package com.hotels.entities.feature;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotels.entities.roomreservationfeature.ReservationFeature;
import com.hotels.entities.room.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "feature")
@Getter
@Setter
@NoArgsConstructor
public class Feature {
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "feature_name")
    private String featureName;

    @ManyToMany(mappedBy = "featureList")
    @JsonIgnore
    private List<Room> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "feature")
    @JsonIgnore
    private List<ReservationFeature> reservationFeatures = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feature feature = (Feature) o;
        return id.equals(feature.id) && featureName.equals(feature.featureName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, featureName);
    }
}
