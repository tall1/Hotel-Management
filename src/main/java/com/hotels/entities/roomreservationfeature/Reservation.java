package com.hotels.entities.roomreservationfeature;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotels.entities.enums.Request;
import com.hotels.entities.enums.RequestImportance;
import com.hotels.entities.userhotel.Hotel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "reservation")
@Data
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

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "reservation_feature",
            joinColumns = {@JoinColumn(name = "reservation_number")},
            inverseJoinColumns = {@JoinColumn(name = "feature_id")}
    )
    @ToString.Exclude
    @JsonIgnore
    private Set<Feature> guestsRequestsSet = new HashSet<>();


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
        return null;
    }
}
