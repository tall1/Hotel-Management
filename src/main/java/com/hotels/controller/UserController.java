package com.hotels.controller;

import com.hotels.entities.userhotel.User;
import com.hotels.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public List<User> getAll() throws SQLException {
        return this.userService.getAll();
    }

//    localhost:8080/api/v1/person/get1/5
    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) throws SQLException {
        return userService.getUserById(id);
    }

    /*//http://localhost:8080/api/v1/person/get2?id=5
    @GetMapping
    public User getPersonById2(int id) throws SQLException {
        return userService.getPersonById(id);
    }*/

    @PostMapping
    public void insertUser(User user) throws SQLException {
        userService.insertUser(user);
    }

    @PutMapping
    public void updateUser(User user) throws SQLException {
        userService.updateUser(user);
    }

    @DeleteMapping
    public void deleteUser(int id) throws SQLException {
        userService.deleteUser(id);
    }
}
