package com.hotels.service;


import com.hotels.entities.user.UserDTO;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface UserService {
     List<UserDTO> getAll() throws EntityNotFoundException;
     UserDTO getUserById(int id) throws  EntityNotFoundException;
     int getUserIdByEmail(String email) throws EntityNotFoundException;
     boolean verifyEmailPass(String email, String password);
     int insertUser(UserDTO userDTO) throws Exception;
     void updateUser(UserDTO userDTO) throws EntityNotFoundException;
     void deleteUser(int userId) throws EntityNotFoundException;
}
