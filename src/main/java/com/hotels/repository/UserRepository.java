package com.hotels.repository;

import com.hotels.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "select u.hotel_id from hoteldb.user u where u.id = :user_id", nativeQuery = true)
    Optional<Integer> findHotelIdByUserId(@Param("user_id") Integer userId);

    @Query(value = "select u.id from hoteldb.user u where u.email = :email", nativeQuery = true)
    Optional<Integer> findUserIdByEmail(@Param("email") String email);

    @Query(value = "select count(*) from hoteldb.user u where u.email = :email", nativeQuery = true)
    int findAmountOfEmails(@Param("email") String email);

    @Query(value = "select count(*) from hoteldb.user u where u.email = :email and u.password = :password", nativeQuery = true)
    int findAmountOfEmailAndPasswordCombinations(@Param("email") String email, @Param("password") String password);
}
