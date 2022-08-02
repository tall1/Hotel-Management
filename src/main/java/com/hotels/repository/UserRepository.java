package com.hotels.repository;

import com.hotels.entities.userhotel.Hotel;
import com.hotels.entities.userhotel.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
