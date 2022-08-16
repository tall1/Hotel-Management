package com.hotels.service;


import com.hotels.entities.user.UserDTO;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface UserService {
     List<UserDTO> getAll() throws EntityNotFoundException;
     UserDTO getUserById(int id) throws  EntityNotFoundException;
     void insertUser(UserDTO userDTO) throws EntityNotFoundException;
     void updateUser(UserDTO userDTO) throws EntityNotFoundException;
     void deleteUser(int userId) throws EntityNotFoundException;
}
