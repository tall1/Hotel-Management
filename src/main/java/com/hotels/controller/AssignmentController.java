package com.hotels.controller;

import com.hotels.entities.assignment.AssignmentDTO;
import com.hotels.entities.task.status.TaskStatus;
import com.hotels.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping(path = "/assignments")
@CrossOrigin(origins = "http://localhost:3000")
public class AssignmentController {
    AssignmentService assignmentService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping("/get_status/{taskId}")
    public TaskStatus getStatus(@PathVariable long taskId) throws EntityNotFoundException {
        return assignmentService.getTaskStatus(taskId);
    }

    @GetMapping("/get_assignment/{taskId}")
    public AssignmentDTO getAssignment(@PathVariable long taskId) {
        return assignmentService.getAssignment(taskId);
    }

    @PutMapping("/update_room_available_dates")
    public void updateRoomsAvailableDate(@RequestBody AssignmentDTO assignmentDTO) throws EntityNotFoundException {
        assignmentService.updateRoomsAvailableDate(assignmentDTO);
    }
}

