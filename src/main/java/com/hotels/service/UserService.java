package com.hotels.service;


import com.hotels.entities.userhotel.User;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface UserService {
     List<User> getAll() throws EntityNotFoundException;
     User getUserById(int id) throws  EntityNotFoundException;
     void insertUser(User user) throws EntityNotFoundException;
     void updateUser(User user) throws EntityNotFoundException;
     void deleteUser(int id) throws EntityNotFoundException;
}
