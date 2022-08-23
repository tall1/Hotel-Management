package com.hotels.entities.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotels.entities.feature.Feature;
import com.hotels.entities.hotel.Hotel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room")
@Getter
@Setter
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
    private LocalDate availableDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "room_feature",
            joinColumns = {@JoinColumn(name = "room_id")},
            inverseJoinColumns = {@JoinColumn(name = "feature_id")}
    )
    private List<Feature> featureList = new ArrayList<>();

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

    public Room(Integer id,
                Integer roomNumber,
                Hotel hotel,
                Integer roomCapacity,
                List<Feature> featureSet) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.hotel = hotel;
        this.floorNumber = Integer.parseInt(String.valueOf(String.valueOf(this.roomNumber).charAt(0)));
        this.roomCapacity = roomCapacity;
        this.availableDate = LocalDate.now();
        this.featureList = new ArrayList<>(featureSet);
    }

    public boolean getIsAvailable() {
        return this.availableDate.isBefore(LocalDate.now()) || this.availableDate.isEqual(LocalDate.now());
    }

    public Boolean doesHaveFeature(Feature feature) {
        return this.featureList.contains(feature);
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomNumber=" + roomNumber +
                ", hotel=" + hotel.getId() +
                ", floorNumber=" + floorNumber +
                ", roomCapacity=" + roomCapacity +
                ", availableDate=" + availableDate +
                '}';
    }
}
