package com.hotels.controller;

import com.hotels.entities.user.User;
import com.hotels.exceptions.ResourceNotFoundException;
import com.hotels.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public List<User> getAll() throws EntityNotFoundException {
        return this.userService.getAll();
    }

//    localhost:8080/api/v1/person/get1/5
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Integer userId) throws  ResourceNotFoundException {
        return userService.getUserById(userId);
    }

    /*//http://localhost:8080/api/v1/person/get2?id=5
    @GetMapping
    public User getPersonById2(int id) throws EntityNotFoundException{
        return userService.getPersonById(id);
    }*/

    @PostMapping
    public void insertUser(User user) throws EntityNotFoundException{
        userService.insertUser(user);
    }

    @PutMapping
    public void updateUser(User user) throws EntityNotFoundException{
        userService.updateUser(user);
    }

    @DeleteMapping
    public void deleteUser(int id) throws EntityNotFoundException{
        userService.deleteUser(id);
    }
}
