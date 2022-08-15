package com.hotels.service;

import com.hotels.assignment.SimpleAssignment;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

public interface AssignmentService {

    SimpleAssignment computeAssignmentByDate(Integer userId, LocalDate date) throws EntityNotFoundException;

    void updateRoomsAvailableDate(SimpleAssignment chosenAssignment) throws EntityNotFoundException;
}
