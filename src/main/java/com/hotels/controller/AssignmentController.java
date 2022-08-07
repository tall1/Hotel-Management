package com.hotels.controller;

import com.hotels.assignment.SimpleAssignment;
import com.hotels.service.AssignmentService;
import com.hotels.service.utils.EngineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.sql.SQLException;

@RestController
@RequestMapping(path = "/assignments")
public class AssignmentController {

    @Value("${times.num}")
    private Integer timesNum;

    AssignmentService assignmentService;

    @PostConstruct
    public void bla() {
        System.out.println(timesNum);
    }

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    //    localhost:8080/api/v1/person/get1/5
    @GetMapping("/{userId}")
    public SimpleAssignment getAssignment(@PathVariable Integer userId) throws SQLException {
        return assignmentService.getAssignment(userId);
    }

    @PostMapping
    public void insertEngineProperties(@RequestBody EngineDTO eDto) throws SQLException {
        assignmentService.insertEngineData(eDto);
    }

    @PutMapping
    public void updateEngineProperties(@RequestBody EngineDTO eDto) throws SQLException {
        assignmentService.updateEngineData(eDto);
    }

    @DeleteMapping
    public void deleteEngineProperties(@RequestBody Integer userId) throws SQLException {
        assignmentService.deleteEngineData(userId);
    }
}

