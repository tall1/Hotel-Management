package com.hotels.service;


import com.hotels.entity.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
     List<User> getAll() throws SQLException;
     User getUserById(int id) throws SQLException;
     void insertUser(User user) throws SQLException;
     void updateUser(User user) throws SQLException;
     void deleteUser(int id) throws SQLException;
}
