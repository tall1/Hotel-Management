package com.hotels.entities.roomreservationfeature;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="feature")
@Data
public class Feature {
    @Id
    @Column(name="id")
    private Integer Id;
    @Column(name="feature_name")
    private String featureName;

    @ManyToMany(mappedBy = "features")
    @JsonIgnore
    private Set<Room> rooms = new HashSet<>();

    @ManyToMany(mappedBy = "guestsRequestsSet")
    @JsonIgnore
    private Set<Reservation> reservations = new HashSet<>();
}
