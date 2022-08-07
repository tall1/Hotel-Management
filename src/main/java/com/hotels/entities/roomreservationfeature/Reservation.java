package com.hotels.entities.roomreservationfeature;

import com.hotels.entities.userhotel.Hotel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "checkin")
    private LocalDate checkin;
    @Column(name = "checkout")
    private LocalDate checkout;
    @OneToMany(mappedBy = "reservation")
    private List<ReservationFeature> reservationFeatures = new ArrayList<>();


    public Reservation(Integer reservationNumber,
                       Hotel hotel,
                       String guestName,
                       Integer guestsAmount,
                       LocalDate checkin,
                       LocalDate checkout,
                       List<ReservationFeature> reservationFeatures) {
        this.reservationNumber = reservationNumber;
        this.hotel = hotel;
        this.guestName = guestName;
        this.guestsAmount = guestsAmount;
        this.checkin = checkin;
        this.checkout = checkout;
        this.reservationFeatures = reservationFeatures;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationNumber=" + reservationNumber +
                ", guestName='" + guestName + '\'' +
                ", guestsAmount=" + guestsAmount +
                '}';
    }
}
