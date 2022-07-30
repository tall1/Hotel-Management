package com.hotels.entities.roomreservationfeature;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotels.entities.enums.Request;
import com.hotels.entities.enums.RequestImportance;
import com.hotels.entities.userhotel.Hotel;
import lombok.*;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
public class Reservation {
    @Id
    @Column(name = "reservation_number")
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer reservationNumber;
    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;
    @Column(name = "guest_name")
    private String guestName;
    @Column(name = "guests_amount")
    private Integer guestsAmount;

    /*@ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "reservation_feature",
            joinColumns = {@JoinColumn(name = "reservation_number")},
            inverseJoinColumns = {@JoinColumn(name = "feature_id")}
    )
    @ToString.Exclude
    private Set<Feature> guestsRequestsSet = new HashSet<>();*/

    @OneToMany(mappedBy = "reservation")
    private Set<ReservationFeature> reservationFeatures = new HashSet<>();


    // Erase later:
    @Transient
    @JsonIgnore
    private Map<Request, RequestImportance> guestsRequests;
    @Transient
    @JsonIgnore
    private static int reservationId = 100;


    public Reservation(int guestsAmount) {
        this.reservationNumber = reservationId++;
        this.guestsAmount = guestsAmount;
        this.guestsRequests = new HashMap<>();
        for (Request request : Request.values()) {
            this.guestsRequests.put(request, Math.random() > 0.33 ? Math.random() > 0.5 ? RequestImportance.MUST : RequestImportance.NICE_TO_HAVE : RequestImportance.NOT_IMPORTANT);
        }
    }

    public RequestImportance getImportance(Request request) {
        return guestsRequests.get(request);
    }
    public RequestImportance getImportance(Feature feature) {
        for (ReservationFeature reservationFeature : this.reservationFeatures ) {
            if(reservationFeature.getFeature().equals(feature)){
                switch(reservationFeature.getImportance()){
                    case 0:
                        return RequestImportance.NOT_IMPORTANT;
                    case 1:
                        return RequestImportance.NICE_TO_HAVE;
                    case 2:
                        return RequestImportance.MUST;
                }
            }
        }
        return RequestImportance.NOT_IMPORTANT;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationNumber=" + reservationNumber +
                ", hotel=" + hotel +
                ", guestName='" + guestName + '\'' +
                ", guestsAmount=" + guestsAmount +
                ", reservationFeatures=" + reservationFeatures +
                '}';
    }
}
