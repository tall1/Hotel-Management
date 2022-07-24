package com.hotels.entities.roomreservationfeature;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotels.entities.enums.Request;
import com.hotels.entities.userhotel.Hotel;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "room")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonIgnore
    private Integer id;

    @Column(name = "room_number")
    private Integer roomNumber;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Column(name = "floor_number")
    private Integer floorNumber;

    @Column(name = "capacity")
    private Integer roomCapacity;

    @Column(name = "available_date")
    @Temporal(TemporalType.DATE)
    private Date availableDate;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "room_feature",
            joinColumns = {@JoinColumn(name = "room_id")},
            inverseJoinColumns = {@JoinColumn(name = "feature_id")}
    )
    @ToString.Exclude
    private Set<Feature> features = new HashSet<>();


    // Eventually erase:
    @Transient
    @ToString.Exclude
    @JsonIgnore
    private static int counter = 1;
    @Transient
    @ToString.Exclude
    @JsonIgnore
    private Map<Request, Boolean> requestsMap;
    @Transient
    @ToString.Exclude
    @JsonIgnore
    private Boolean isAvailable;

    // Maybe add:
    //private RoomType roomType;
    //private RoomFacingDirection roomDirection;

    /* public enum RoomFacingDirection {
        North,
        South,
        West,
        East
    }

    public enum RoomType { // Important?
        Deluxe,
        Executive,
        Club,
        HandicappedFriendly,
        DeluxeSuite,
        ExecutiveSuite,
        ClubSuite,
        PresidentSuite
    }*/

    public Room(int roomCapacity) {
        roomNumber = counter++;
        this.roomCapacity = roomCapacity;
        this.floorNumber = Integer.parseInt(String.valueOf(String.valueOf(roomNumber).charAt(0)));
        this.requestsMap = new HashMap<>();
        for (Request request : Request.values()) {
            requestsMap.put(request, Math.random() > 0.5);
        }
        this.isAvailable = true;
    }

    public Boolean doesComplyWithRequest(Request request) {
        return this.requestsMap.get(request);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Room room = (Room) o;
        return id != null && Objects.equals(id, room.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
