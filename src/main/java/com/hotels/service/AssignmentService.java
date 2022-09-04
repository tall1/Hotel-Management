package com.hotels.service;

import com.hotels.entities.assignment.AssignmentDTO;
import com.hotels.entities.task.status.TaskStatus;
import com.hotels.exceptions.NoAssignmentsForTaskException;

import javax.persistence.EntityNotFoundException;

public interface AssignmentService {

    AssignmentDTO getAssignment(Long taskId) throws NoAssignmentsForTaskException;

    TaskStatus getTaskStatus(Long taskId) throws EntityNotFoundException;

    void updateRoomsAvailableDate(AssignmentDTO chosenAssignment) throws EntityNotFoundException;

    void runTasks();
}
