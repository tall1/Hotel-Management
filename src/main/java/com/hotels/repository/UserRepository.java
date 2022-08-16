package com.hotels.repository;

import com.hotels.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "select u.hotel_id from hoteldb.user u where u.id = :user_id", nativeQuery = true)
    Optional<Integer> findHotelIdByUserId(@Param("user_id") Integer userId);

    @Transactional
    @Modifying
    @Query(value = "delete from user u where u.id = :user_id", nativeQuery = true)
    void deleteUserById(@Param("user_id") Integer userId);
}
