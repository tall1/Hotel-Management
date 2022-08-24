package com.hotels.entities.hotel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.hotels.entities.task.Task;
import com.hotels.entities.reservation.Reservation;
import com.hotels.entities.room.Room;
import com.hotels.entities.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*; // TODO *
import java.util.List;

@Entity
@Table(name = "hotel")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id") // This annotation breaks the loop.
@Getter
@Setter
@NoArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="name")
    private String hotelName;
    @Column(name="num_of_floors")
    private Integer numOfFloors;
    @Column(name="num_of_rooms")
    private Integer numOfRooms;

    @OneToOne(mappedBy = "hotel")
    @JsonIgnore
    private User admin;

    @OneToMany(mappedBy = "hotel")
    @JsonIgnore
    private List<Room> rooms;

    @OneToMany(mappedBy = "hotel")
    @JsonIgnore
    private List<Reservation> reservations;
}
