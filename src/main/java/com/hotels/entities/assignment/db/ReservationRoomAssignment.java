package com.hotels.entities.assignment.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "reservation_room_assignment")
@Getter
@Setter
@NoArgsConstructor
public class ReservationRoomAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relation_id")
    private Long relationId;
    @Column(name = "reservation_number")
    private Long reservationNumber;
    @Column(name = "room_id")
    private Long roomNumber;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "assignment_id")
    private AssignmentsDB assignment;
}
