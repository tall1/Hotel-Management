package com.hotels.controller;

import com.hotels.entities.user.UserDTO;
import com.hotels.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public List<UserDTO> getAll() throws EntityNotFoundException {
        return this.userService.getAll();
    }

    //    localhost:8080/api/v1/person/get1/5
    @GetMapping("/{userId}")
    public UserDTO getUserById(@PathVariable int userId) throws EntityNotFoundException {
        return userService.getUserById(userId);
    }

    @GetMapping("/get_id_by_email")
    public int getUserIdByEmail(@RequestParam String email) throws EntityNotFoundException {
        return userService.getUserIdByEmail(email);
    }

    @GetMapping("/verify_email_password")
    public boolean verifyUserPass(@RequestParam String email, @RequestParam String password) {
        return userService.verifyEmailPass(email, password);
    }

    /*//http://localhost:8080/api/v1/person/get2?id=5
    @GetMapping
    public User getPersonById2(@RequestParam int id) throws EntityNotFoundException{
        return userService.getPersonById(id);
    }*/

    @PostMapping
    public int insertUser(@RequestBody UserDTO userDTO) throws Exception {
        return userService.insertUser(userDTO);
    }

    @PutMapping
    public void updateUser(@RequestBody UserDTO userDTO) throws EntityNotFoundException {
        userService.updateUser(userDTO);
    }

    @DeleteMapping
    public void deleteUser(int id) throws EntityNotFoundException {
        userService.deleteUser(id);
    }
}
