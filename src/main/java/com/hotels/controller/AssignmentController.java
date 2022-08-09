package com.hotels.controller;

import com.hotels.assignment.SimpleAssignment;
import com.hotels.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
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

    //    localhost:8080/api/v1/person/get1/5
    @GetMapping("/{userId}")
    public SimpleAssignment getAssignment(@PathVariable Integer userId) throws SQLException {
        return assignmentService.computeAssignmentByDate(userId, null);
    }

    /*//http://localhost:8080/api/v1/person/get2?id=5*/
    @GetMapping("/byDate")
    public SimpleAssignment getAssignment(int userId, int day, int month, int year) throws SQLException {
        return assignmentService.computeAssignmentByDate(userId, LocalDate.of(year, month, day));
    }

    @PutMapping("/update_room_available_dates")
    public void updateRoomsAvailableDate(@RequestBody SimpleAssignment simpleAssignment) throws SQLException {
        assignmentService.updateRoomsAvailableDate(simpleAssignment);
    }
}

