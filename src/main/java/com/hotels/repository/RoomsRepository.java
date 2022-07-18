package com.hotels.repository;

import com.hotels.assignment.entities.Room;
import com.hotels.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomsRepository extends JpaRepository<Room, Integer> {

}
