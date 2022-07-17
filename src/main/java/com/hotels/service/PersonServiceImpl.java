package com.hotels.service;
import com.hotels.entity.Person;
import com.hotels.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.SQLException;
import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {
    private PersonRepository personRepository;

    @PostConstruct
    public void init453534(){
        // here put any after construction operations
        System.out.println("@PostConstruct");
    }
    @PreDestroy
    public void  exitAll123(){
        System.out.println("@PreDestroy");
    }

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<Person> getAll() throws SQLException {
        return personRepository.findAll();
    }

    @Override
    public Person getPersonById(int id) throws SQLException {
        return personRepository.findById(id).get();
    }

    @Override
    public void insertPerson(Person p) throws SQLException {
        personRepository.save(p);
    }

    @Override
    public void updatePerson(Person p) throws SQLException {
        personRepository.save(p);
    }

    @Override
    public void deletePerson(int id) throws SQLException {
        personRepository.deleteById(id);
    }
}
