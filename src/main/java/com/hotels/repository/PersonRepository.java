package com.hotels.repository;

import com.hotels.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
/*    List<Person> getAll() throws SQLException;
    Person getPersonById(int id) throws SQLException;
    void insertPerson(String name, int age) throws SQLException;
    void updatePerson(int id, String name, int age) throws SQLException;
    void deletePerson(int id) throws SQLException;*/
}
