package com.hotels.controller;

import com.hotels.entities.assignment.AssignmentDTO;
import com.hotels.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping(path = "/assignments")
public class AssignmentController {
    /*@Value("${times.num}")
    private Integer timesNum;*/

    AssignmentService assignmentService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping("/get_status/{taskId}")
    public String getStatus(@PathVariable long taskId) throws Exception {
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

