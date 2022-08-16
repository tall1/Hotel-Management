package com.hotels.entities.roomreservationfeature;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotels.entities.feature.Feature;
import com.hotels.entities.reservation.Reservation;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "reservation_feature")
@Getter
@Setter
@NoArgsConstructor
public class ReservationFeature {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "reservation_number")
    @JsonIgnore
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "feature_id")
    private Feature feature;

    @Column(name = "importance")
    private Integer importance;
}
