package com.hotels.service;

import com.hotels.entities.assignment.AssignmentDTO;
import com.hotels.exceptions.NoAssignmentsForTaskException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

public interface AssignmentService {

    AssignmentDTO computeAssignmentByDate(Integer userId, LocalDate date) throws EntityNotFoundException;

    AssignmentDTO getAssignment(Long taskId) throws NoAssignmentsForTaskException;

    String getTaskStatus(Long taskId) throws Exception;

    void updateRoomsAvailableDate(AssignmentDTO chosenAssignment) throws EntityNotFoundException;

    void runTasks();
}
