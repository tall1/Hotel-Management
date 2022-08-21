package com.hotels.controller;

import com.hotels.entities.assignment.AssignmentDTO;
import com.hotels.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

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

    //localhost:8080/api/v1/person/get1/5
    @GetMapping("/{userId}")
    public AssignmentDTO getAssignment(@PathVariable Integer userId) throws  EntityNotFoundException {
        return assignmentService.computeAssignmentByDate(userId, null);
    }

    //http://localhost:8080/api/v1/person/get2?id=5
    @GetMapping("/byDate")
    public AssignmentDTO getAssignment(int userId, int day, int month, int year) throws EntityNotFoundException{
        return assignmentService.computeAssignmentByDate(userId, LocalDate.of(year, month, day));
    }

    @PutMapping("/update_room_available_dates")
    public void updateRoomsAvailableDate(@RequestBody AssignmentDTO assignmentDTO) throws EntityNotFoundException{
        assignmentService.updateRoomsAvailableDate(assignmentDTO);
    }
}

