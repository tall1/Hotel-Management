package com.hotels.service;

import com.hotels.entities.assignment.AssignmentDTO;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

public interface AssignmentService {

    AssignmentDTO computeAssignmentByDate(Integer userId, LocalDate date) throws EntityNotFoundException;

    void updateRoomsAvailableDate(AssignmentDTO chosenAssignment) throws EntityNotFoundException;
}
