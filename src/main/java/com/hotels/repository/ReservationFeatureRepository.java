package com.hotels.repository;

import com.hotels.entities.roomreservationfeature.ReservationFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationFeatureRepository extends JpaRepository<ReservationFeature, Integer> {
    @Query(value = "select rf.id from reservation_feature rf where rf.reservation_number = :reservation_num and rf.feature_id = :feature_id", nativeQuery = true)
    int findReservationFeatureIdByResNumAndFeatureId(@Param("reservation_num") Integer resNum, @Param("feature_id") int featureId);

    @Modifying
    @Query(value = "delete from reservation_feature rf where rf.reservation_number = :reservation_number", nativeQuery = true)
    void deleteReservationFeatureByReservationNumber(@Param("reservation_number") int resNum);
}
