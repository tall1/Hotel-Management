package com.hotels.entities.roomreservationfeature;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany(mappedBy = "features")
    @JsonIgnore
    private Set<Room> rooms = new HashSet<>();

    /*@ManyToMany(mappedBy = "guestsRequestsSet")
    @JsonIgnore
    private Set<Reservation> reservations = new HashSet<>();*/

    @OneToMany(mappedBy = "feature")
    @JsonIgnore
    private Set<ReservationFeature> reservationFeatures = new HashSet<>();
}
