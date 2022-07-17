package com.hotels.repository;

import com.hotels.entity.Person;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//@Repository
public class PersonRepositoryImpl { // JDBC!!!

    private Connection con;
    private Statement stmt;
    private PreparedStatement getPersonByIdPreparedStatement;
    private PreparedStatement insertPreparedStatement;
    private PreparedStatement updatePreparedStatement;
    private PreparedStatement deletePreparedStatement;
    private final String getStr = "SELECT * FROM people WHERE id = ?";
    private final String insertStr = "INSERT INTO people(name, age) VALUES (?, ?)";
    private final String updateStr = "update people set name = ?, age = ? where id = ?";
    private final String deleteStr = "DELETE FROM people WHERE id = ?";

    public PersonRepositoryImpl() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //"com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/demodb", "root", "password");
            stmt = con.createStatement();
            getPersonByIdPreparedStatement = con.prepareStatement(getStr);
            insertPreparedStatement = con.prepareStatement(insertStr);
            updatePreparedStatement = con.prepareStatement(updateStr);
            deletePreparedStatement = con.prepareStatement(deleteStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() throws SQLException {
        this.con.close();
    }

    public List<Person> getAll() throws SQLException {
        List<Person> personList = new ArrayList<>();
        Person p;
        ResultSet rs = stmt.executeQuery("select * from people");
        while (rs.next()) {
            p = new Person(rs.getInt("id"), rs.getString("name"), rs.getInt("age"));
            personList.add(p);
        }
        return personList;
    }

    public Person getPersonById(int id) throws SQLException {
        getPersonByIdPreparedStatement.setInt(1, id);
        ResultSet rs = getPersonByIdPreparedStatement.executeQuery();
        Person p = new Person();
        while (rs.next()) {
            p.setId(rs.getInt("id"));
            p.setName(rs.getString("name"));
            p.setAge(rs.getInt("age"));
        }
        return p;
    }

    public void insertPerson(String name, int age) throws SQLException {
        insertPreparedStatement.setString(1, name);
        insertPreparedStatement.setInt(2, age);
        insertPreparedStatement.executeUpdate();
    }

    public void updatePerson(int id, String name, int age) throws SQLException {
        updatePreparedStatement.setString(1, name);
        updatePreparedStatement.setInt(2, age);
        updatePreparedStatement.setInt(3, id);
        updatePreparedStatement.executeUpdate();
    }

    public void deletePerson(int id) throws SQLException {
        deletePreparedStatement.setInt(1, id);
        deletePreparedStatement.executeUpdate();
    }


}
