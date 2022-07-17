package com.hotels.controller;

import com.hotels.entity.Person;
import com.hotels.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/person")
public class PersonController {
    private PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/")
    public List<Person> getAll() throws SQLException {
        return this.personService.getAll();
    }

//    localhost:8080/api/v1/person/get1/5
    @GetMapping("/{id}")
    public Person getPersonById1(@PathVariable int id) throws SQLException {
        return personService.getPersonById(id);
    }
/*
//    http://localhost:8080/api/v1/person/get2?id=5
    @GetMapping
    public Person getPersonById2(int id) throws SQLException {
        return personService.getPersonById(id);
    }*/

    @PostMapping
    public void insertPerson(Person p) throws SQLException {
        personService.insertPerson(p);
    }

    @PutMapping
    public void updatePerson(Person p) throws SQLException {
        personService.updatePerson(p);
    }

    @DeleteMapping
    public void deletePerson(int id) throws SQLException {
        personService.deletePerson(id);
    }
}
