package com.hotels.repository;

import com.hotels.entities.userhotel.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
/*    List<User> getAll() throws SQLException;
    User getPersonById(int id) throws SQLException;
    void insertPerson(String name, int age) throws SQLException;
    void updatePerson(int id, String name, int age) throws SQLException;
    void deletePerson(int id) throws SQLException;*/
}
