package com.hotels.service;


import com.hotels.entity.Person;

import java.sql.SQLException;
import java.util.List;

public interface PersonService {
     List<Person> getAll() throws SQLException;
     Person getPersonById(int id) throws SQLException;
     void insertPerson(Person p) throws SQLException;
     void updatePerson(Person p) throws SQLException;
     void deletePerson(int id) throws SQLException;

}
