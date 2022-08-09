package com.hotels.service;

import com.hotels.assignment.SimpleAssignment;

import java.sql.SQLException;
import java.time.LocalDate;

public interface AssignmentService {

    SimpleAssignment computeAssignmentByDate(Integer userId, LocalDate date) throws SQLException;

    void updateRoomsAvailableDate(SimpleAssignment chosenAssignment) throws SQLException;
}
